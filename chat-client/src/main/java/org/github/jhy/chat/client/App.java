package org.github.jhy.chat.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.github.jhy.chat.client.gui.IController;
import org.github.jhy.chat.client.gui.controller.LoginViewController;
import org.github.jhy.chat.client.gui.controller.MainController;
import org.github.jhy.chat.client.netty.ApplicationContext;

@Slf4j
public class App extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        ApplicationContext.setApp(this);
        this.stage = stage;
        showLoginView();
    }

    public void showLoginView(){
        show(new LoginViewController());
    }

    public void showMainView(){
        show(new MainController());
    }

    private void show(IController controller){
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.load(controller.getResource(App.class).toExternalForm());
        this.addWebEngineListener(engine, controller);

        Scene scene = new Scene(webView, 640, 480);
        stage.setTitle(MainController.TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 添加监听，加载web后回调，添加监听事件
     *
     * @param engine web引擎
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
        if (log.isDebugEnabled()) {
            engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    engine.executeScript("var firebug=document.createElement('script');firebug.setAttribute('src','https://lupatec.eu/getfirebug/firebug-lite-compressed.js');document.body.appendChild(firebug);(function(){if(window.firebug.version){firebug.init();}else{setTimeout(arguments.callee);}})();void(firebug);");
                }
            });
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
