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
    public void handlerLogin(String username, String password) throws InterruptedException {
        // 跳转页面
        NettyChatClient client = new NettyChatClient("127.0.0.1", 9999);
        client.run();

        EventMessage eventMessage = client.sendRegister(new MessageUser(username, password));
        System.out.println(eventMessage);

        ApplicationContext.setClient(client);
        ApplicationContext.getApp().showMainView();
    }


    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

}
