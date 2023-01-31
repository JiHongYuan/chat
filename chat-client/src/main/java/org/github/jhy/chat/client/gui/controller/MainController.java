package org.github.jhy.chat.client.gui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.App;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.netty.ApplicationContext;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.model.MessageUser;

import java.net.URL;
import java.util.List;

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
     * 处理消息刷新
     */
    public void handlerMessageRefresh(EventMessage eventMessage) {
        try {
            jsObject.call("refreshMessage", objectMapper.writeValueAsString(eventMessage));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 处理消息发送
     * */
    public void handlerSend(String to, String msg) {
        final MessageUser user = ApplicationContext.getUser();
        try {
            EventMessage eventMessage = ApplicationContext.getClient().sendMsg(user.getUsername(), to, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

    @Override
    public void initialize() {
        handlerUserList();
        synchronized (MainController.class) {
            if (messageRefreshThread != null) {
                messageRefreshThread.interrupt();
            }
            messageRefreshThread = new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        EventMessage eventMessage = ApplicationContext.messageQueue.take();
                        App.refreshUI(() -> handlerMessageRefresh(eventMessage));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
            messageRefreshThread.start();
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

}
