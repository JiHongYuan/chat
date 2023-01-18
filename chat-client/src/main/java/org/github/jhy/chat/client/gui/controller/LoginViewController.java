package org.github.jhy.chat.client.gui.controller;


import org.github.jhy.chat.client.netty.ApplicationContext;
import org.github.jhy.chat.client.netty.NettyChatClient;
import org.github.jhy.chat.client.gui.IController;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.model.MessageUser;

import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * @author jihongyuan
 * @date 2023/1/11 16:17
 */
public class LoginViewController implements IController {

    public static final String TITLE = "登录";

    private static final String VIEW_PATH = "/view/LoginView.html";

    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

    /**
     * 处理登录 到主界面的事件
     */
    public synchronized void handlerLogin(String username, String password) throws InterruptedException, ExecutionException {

        // 跳转页面
        NettyChatClient client = new NettyChatClient("127.0.0.1", 9999);
        client.run();

        EventMessage eventMessage = client.sendRegister(new MessageUser(username, password));
        System.out.println(eventMessage);

        ApplicationContext.setClient(client);
        ApplicationContext.getApp().showMainView();
    }

    public void handleForgetPasswordAction() {
    }

    /**
     * 处理登录 到主界面的事件
     */
    public void handleRegistButtonAction() {
    }

}
