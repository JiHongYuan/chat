package org.github.jhy.chat.client.gui.controller;


import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.netty.ApplicationContext;
import org.github.jhy.chat.client.netty.NettyChatClient;
import org.github.jhy.chat.common.EventMessage;
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
    public void handlerLogin(String username, String password){

        // 跳转页面
        NettyChatClient client = ApplicationContext.initClient();

        MessageUser user = new MessageUser(username, password);
        EventMessage eventMessage = client.sendRegister(new MessageUser(username, password));
        // TODO 处理登录返回逻辑

        ApplicationContext.setUser(user);
        // 登录成功, 跳转页面
        ApplicationContext.getApp().go(MainController.class);
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
