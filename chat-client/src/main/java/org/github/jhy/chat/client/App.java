package org.github.jhy.chat.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.util.List;

import org.github.jhy.chat.client.gui.controller.LoginViewController;
import org.github.jhy.chat.client.gui.controller.MainController;
import org.github.jhy.chat.client.netty.ApplicationContext;

public class App extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        ApplicationContext.setApp(this);
        this.stage = stage;
        showLoginView();
    }

    public void showLoginView(){
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        LoginViewController controller = new LoginViewController();
        engine.load(controller.getResource(App.class).toExternalForm());

        this.addWebEngineListener(engine, controller.getWebEngineListener(engine));

        Scene scene = new Scene(webView, 640, 480);
        stage.setTitle(LoginViewController.TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public void showMainView(){
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        MainController controller = new MainController();
        engine.load(controller.getResource(App.class).toExternalForm());

        this.addWebEngineListener(engine, controller.getWebEngineListener(engine));

        Scene scene = new Scene(webView, 640, 480);
        stage.setTitle(LoginViewController.TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 添加监听，加载web后回调，添加监听事件
     *
     * @param engine web引擎
     */
    private void addWebEngineListener(WebEngine engine, List<ChangeListener<? super Worker.State>> changeListeners) {
        for (ChangeListener<? super Worker.State> changeListener : changeListeners) {
            engine.getLoadWorker().stateProperty().addListener(changeListener);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
