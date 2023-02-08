package org.github.jhy.chat.client;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.github.jhy.chat.client.gui.IController;
import org.github.jhy.chat.client.gui.controller.LoginViewController;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jihongyuan
 * @date 2023/1/30 11:08
 */
@Slf4j
public class WebViewManager {
    private final Stage stage;

    public WebViewManager(Stage stage) {
        this.stage = stage;
        go(LoginViewController.class);
    }

    public void go(Class<? extends IController> clazz) {
        IController controller;
        try {
            controller = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        show(controller);
    }

    private void show(IController controller) {
        show(controller.getWidth(), controller.getHeight(), controller);
    }

    private void show(int width, int height, IController controller) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.load(controller.getResource(App.class).toExternalForm());
        this.addWebEngineListener(engine, controller);

        Scene scene = new Scene(webView, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 添加监听，加载web后回调，添加监听事件
     *
     * @param engine     web引擎
     * @param controller view
     */
    private void addWebEngineListener(WebEngine engine, IController controller) {
        for (ChangeListener<? super Worker.State> changeListener : controller.getWebEngineListener(engine)) {
            engine.getLoadWorker().stateProperty().addListener(changeListener);
        }

        engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", controller);

                controller.setWebEngine(engine);
                controller.setJSObject(window);

                // web engine 初始化
                controller.initialize();
            }
        });

        engine.setOnAlert(event -> log.info(event.getData()));
        engine.setOnError(event -> log.error(event.getMessage(), event.getException()));
    }

}
