package org.github.jhy.chat.client.gui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.App;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.ApplicationContext;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.model.MessageUser;

import java.net.URL;
import java.util.*;

/**
 * @author jihongyuan
 * @date 2023/1/11 16:17
 */
@Slf4j
public class MainController extends AbstractController {

    private ObjectMapper objectMapper = new ObjectMapper();

    public static final String TITLE = "主页";

    private static final String VIEW_PATH = "/view/MainView.html";

    private static Thread messageRefreshThread;

    private final Map<String, List<EventMessage>> userMessageMap = new HashMap<>();

    /**
     * 处理用户列表
     */
    public void handlerUserList() {
        final MessageUser user = ApplicationContext.getUser();
        List<MessageUser> users = ApplicationContext.getClient().getUserList(user.getUsername());
        try {
            jsObject.call("showUserList", objectMapper.writeValueAsString(users));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
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
                jsObject.call("refreshMessage", objectMapper.writeValueAsString(eventMessage));
            }
            // TODO 全量刷新
            // else if (eventMessage == null) {
            //    jsObject.call("refreshMessage", objectMapper.writeValueAsString(userMessageMap.get(to)));
            // }
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
        synchronized (MainController.class) {
            if (messageRefreshThread == null) {
                messageRefreshThread = new Thread(() -> {
                    while (!Thread.interrupted()) {
                        try {
                            EventMessage eventMessage = ApplicationContext.MESSAGE_QUEUE.take();
                            App.refreshUI(() -> invokeMessageRefresh(eventMessage));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                });
                messageRefreshThread.start();
            }
        }
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
        handlerUserList();
        try {
            jsObject.call("initialize", objectMapper.writeValueAsString(ApplicationContext.getUser()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

}
