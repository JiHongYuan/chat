package org.github.jhy.chat.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/17 15:29
 */
public interface IController {

    URL getResource(Class<?> contextApp);

    default List<ChangeListener<? super Worker.State>> getWebEngineListener(WebEngine engine) {
        List<ChangeListener<? super Worker.State>> listeners = new ArrayList<>();

        listeners.add((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);
            }
        });
        return listeners;
    }

}
