package org.github.jhy.chat.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.gui.IController;
import org.github.jhy.chat.client.netty.ApplicationContext;

@Slf4j
public class App extends Application {

    private WebViewManager viewManager;

    @Override
    public void start(Stage stage) {
        ApplicationContext.setApp(this);
        this.viewManager = new WebViewManager(stage);
    }

    public void go(Class<? extends IController> clazz) {
        viewManager.go(clazz);
    }

    /**
     * refresh application UI
     */
    public static void refreshUI(Runnable runnable) {
        Platform.runLater(runnable);
    }

    public static void main(String[] args) {
        launch();
    }

}
