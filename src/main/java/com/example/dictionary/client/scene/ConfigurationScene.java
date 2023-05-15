package com.example.dictionary.client.scene;

import com.example.dictionary.client.controller.Client;
import com.example.dictionary.client.controller.ConnectionConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


/**
 * Класс обрабатывающий окно конфигурации сервера.
 */
public class ConfigurationScene {
    private static final String MAIN_WINDOW_PATH = "/com/example/dictionary/main.fxml";
    private static final String SERVER_CONFIG = "server";

    @FXML
    private TextField serverNameInput;

    @FXML
    private Label messageLabel;

    /**
     * При нажатии на кнопку CONFIGURE берем имя сервера и ищем соотв. конфигурацию.
     * При нахождение конфиг. устан. ее клиенту и переходим на главное окно.
     */
    @FXML
    void configure(ActionEvent event) throws IOException {
        String serverName = serverNameInput.getText();
        if (!serverName.isEmpty() || !serverName.isBlank()) {
            Optional<ConnectionConfiguration> configurationOptional = getConfiguration(serverName);
            if (configurationOptional.isPresent()) {
                ConnectionConfiguration configuration = configurationOptional.get();
                Client client = Client.getInstance();
                client.configure(configuration);
                goToMainPage(event);
            } else {
                messageLabel.setText("Can't find such configuration");
            }
        }
    }

    /**
     * Загружает главное окно.
     */
    private void goToMainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_WINDOW_PATH));
        Parent pane = loader.load();
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(pane);
        Stage stage = ((Stage) scene.getWindow());
        stage.setResizable(true);
        stage.setMaximized(true);
        MainScene mainScene = loader.getController();
        mainScene.loadTerms();
    }

    /**
     * Возвращает конфигурацию подключения, если таковая имеется. В ином варианте вернет пустой Optional.
     */
    private Optional<ConnectionConfiguration> getConfiguration(String serverName) {
        Optional<ConnectionConfiguration> configuration = Optional.empty();
        if (serverName.equalsIgnoreCase(SERVER_CONFIG)) {
            configuration = Optional.of(new ConnectionConfiguration("localhost", 9999));
        }
        return configuration;
    }
}
