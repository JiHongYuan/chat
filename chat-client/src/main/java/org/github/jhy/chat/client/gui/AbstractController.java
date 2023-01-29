package org.github.jhy.chat.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;

import java.util.Collections;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/29 23:06
 */
@Slf4j
public abstract class AbstractController implements IController {

    protected WebEngine engine;

    protected JSObject jsObject;

    @Override
    public List<ChangeListener<? super Worker.State>> getWebEngineListener(WebEngine engine) {
        return Collections.emptyList();
    }

    @Override
    public void setWebEngine(WebEngine engine) {
        this.engine = engine;
    }

    @Override
    public void setJSObject(JSObject jsObject) {
        this.jsObject = jsObject;
    }

    @Override
    public void initialize() {
    }

    protected void info(String msg) {
        log.info(msg);
    }

}
