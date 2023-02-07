package org.github.jhy.chat.client.gui.controller;


import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.ApplicationContext;
import org.github.jhy.chat.client.netty.NettyChatClient;
import org.github.jhy.chat.common.Constants;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.Message;
import org.github.jhy.chat.common.model.MessageUser;

import java.net.URL;

/**
 * @author jihongyuan
 * @date 2023/1/11 16:17
 */
@Slf4j
public class LoginViewController extends AbstractController {

    public static final String TITLE = "登录";

    private static final String VIEW_PATH = "/view/LoginView.html";

    /**
     * 处理登录 到主界面的事件
     */
    public void handlerSignIn(String username, String password) {
        handlerSign(username, password, EventType.SIGN_IN);
    }

    /**
     * 处理注册 到主界面的事件
     */
    public void handlerSignUp(String username, String password) {
        handlerSign(username, password, EventType.SIGN_UP);
    }

    public void handlerSign(String username, String password, EventType eventType) {
        // 跳转页面
        NettyChatClient client = ApplicationContext.initClient();

        MessageUser user = new MessageUser(username, password);
        EventMessage requestMsg = EventMessage.builderEventType(eventType);
        requestMsg.setFrom(username);
        requestMsg.setBody(new MessageUser(username, password));

        EventMessage responseMsg = client.sendSync(requestMsg);

        Message message = (Message) responseMsg.getBody();
        // 登录成功, 跳转页面
        if (Constants.SUCCESS.equals(message.getCode())) {
            ApplicationContext.setUser(user);
            ApplicationContext.getApp().go(MainController.class);
        } else {
            jsObject.call("fail", message.getCode());
        }
    }

    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

    @Override
    public int getWidth() {
        return 640;
    }

    @Override
    public int getHeight() {
        return 480;
    }

}
