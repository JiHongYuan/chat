package org.github.jhy.chat.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/17 15:29
 */
public interface IController {

    URL getResource(Class<?> contextApp);

    default List<ChangeListener<? super Worker.State>> getWebEngineListener(WebEngine engine) {
        return Collections.emptyList();
    }

    default void setWebEngine(WebEngine engine){}

    default void setJSObject(JSObject jsObject){}

}
