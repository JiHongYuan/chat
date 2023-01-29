package org.github.jhy.chat.client.gui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.gui.AbstractController;
import org.github.jhy.chat.client.netty.ApplicationContext;
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

    // TODO
    public void handlerUserList() {
        List<MessageUser> users = ApplicationContext.getClient().getUserList("user");
        try {
            jsObject.call("showUserList", objectMapper.writeValueAsString(users));
        } catch (JsonProcessingException e) {
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
    }

}
