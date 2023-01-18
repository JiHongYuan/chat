package org.github.jhy.chat.client.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import org.github.jhy.chat.client.gui.IController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/11 16:17
 */
public class MainController implements IController {

    public static final String TITLE = "主页";

    private static final String VIEW_PATH = "/view/MainView.html";

    @Override
    public URL getResource(Class<?> contextApp) {
        return contextApp.getResource(VIEW_PATH);
    }

}
