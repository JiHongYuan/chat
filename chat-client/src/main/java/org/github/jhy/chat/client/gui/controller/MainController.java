package org.github.jhy.chat.client.gui.controller;

import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.App;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.ApplicationContext;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.JSON;
import org.github.jhy.chat.common.model.MessageUser;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jihongyuan
 * @date 2023/1/11 16:17
 */
@Slf4j
public class MainController extends AbstractController {

    private static final String VIEW_PATH = "/view/MainView.html";

    private boolean initialized = false;

    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

    private final Map<String, List<EventMessage>> userMessageMap = new HashMap<>();

    /**
     * 处理用户列表
     */
    public void refreshUserList() {
        try {
            final MessageUser user = ApplicationContext.getUser();
            List<MessageUser> users = ApplicationContext.getClient().getUserList(user.getUsername());
            App.refreshUI(() -> jsObject.call("showUserList", JSON.toString(users)));
        } catch (Exception e) {
            log.error("Refresh main user list error", e);
        }
    }

    /**
     * 处理消息发送
     */
    public void handlerSend(String to, String msg) {
        final MessageUser user = ApplicationContext.getUser();
        try {
            EventMessage eventMessage = ApplicationContext.getClient().sendMsg(user.getUsername(), to, msg);
            refreshMessage(eventMessage.getTo(), eventMessage);
            List<EventMessage> messages = userMessageMap.computeIfAbsent(eventMessage.getTo(), k -> new ArrayList<>());
            messages.add(eventMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 提供UI刷新方法
     */
    public void refreshMessage(String to) {
        refreshMessage(to, null);
    }

    private void refreshMessage(String to, EventMessage eventMessage) {
        String selectedTo = getTo();
        try {
            // 增量刷新
            if (to.equals(selectedTo)) {
                App.refreshUI(() -> jsObject.call("refreshMessage", JSON.toString(eventMessage)));
            } else if (eventMessage == null) {
                App.refreshUI(() -> jsObject.call("refreshMessageList", JSON.toString(userMessageMap.get(selectedTo))));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 执行消息刷新
     */
    private void invokeMessageRefresh(EventMessage eventMessage) {
        refreshMessage(eventMessage.getFrom(), eventMessage);
        List<EventMessage> messages = userMessageMap.computeIfAbsent(eventMessage.getFrom(), k -> new ArrayList<>());
        messages.add(eventMessage);
    }

    /**
     * 获取当前聊天目标
     */
    private String getTo() {
        return (String) jsObject.call("getSelectedName");
    }

    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

    @Override
    public void initialize() {
        initializeGUI();

        if (initialized) return;

        service.scheduleAtFixedRate(() -> {
            try {
                EventMessage eventMessage = ApplicationContext.MESSAGE_QUEUE.poll();
                if(eventMessage != null){
                    App.refreshUI(() -> invokeMessageRefresh(eventMessage));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, 0, 3, TimeUnit.SECONDS);
        // TODO 刷新用户会把页面选中刷新
        service.scheduleAtFixedRate(this::refreshUserList, 0, 180, TimeUnit.SECONDS);

        initialized = true;
    }


    @Override
    public int getWidth() {
        return 1080;
    }

    @Override
    public int getHeight() {
        return 720;
    }

    private void initializeGUI() {
        refreshUserList();
        jsObject.call("initialize", JSON.toString(ApplicationContext.getUser()));
    }

}
