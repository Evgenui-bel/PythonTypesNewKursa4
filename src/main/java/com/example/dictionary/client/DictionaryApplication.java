package com.example.dictionary.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DictionaryApplication extends Application {

    private static final String CONFIG_WINDOW_PATH = "/com/example/dictionary/configuration.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication.class.getResource(CONFIG_WINDOW_PATH));
        Pane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("OSI Model dictionary");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
