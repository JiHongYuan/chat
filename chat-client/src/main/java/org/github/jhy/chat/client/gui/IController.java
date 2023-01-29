package org.github.jhy.chat.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/17 15:29
 */
public interface IController {

    /**
     * 获取 view资源
     *
     * @param contextApp app class
     * @return view URL
     */
    URL getResource(Class<?> contextApp);

    /**
     * 获取 web engine 监听器列表
     *
     * @param engine web engine
     * @return listener
     */
    List<ChangeListener<? super Worker.State>> getWebEngineListener(WebEngine engine);

    void setWebEngine(WebEngine engine);

    void setJSObject(JSObject jsObject);

    /**
     * web engine 初始化
     */
    void initialize();

}
