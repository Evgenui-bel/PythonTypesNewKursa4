package com.example.dictionary.client.scene;

import com.example.dictionary.client.controller.ClientController;
import com.example.dictionary.server.model.entity.Term;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Обработчик главного окна приложения.
 */
public class MainScene {
    @FXML
    private VBox termCards;

    @FXML
    private TextField searchInput;

    @FXML
    private ScrollPane scrollPane;

    /**
     * Загружаем термины при инициализации окна.
     */
    @FXML
    void initialize() {
        changeScrollPaneSpeed();
    }


    private void changeScrollPaneSpeed() {
        double speed = 0.00001;
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * speed;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
        });
    }

    /**
     * Загружает термины и отображаем на главном окне.
     */
    public void loadTerms() {
        searchInput.clear();
        ClientController termRepository = new ClientController();
        try {
            List<Term> terms = termRepository.getTerms();
            termCards.getChildren().clear();
            terms.forEach(this::createTermCard);
        } catch (IOException e) {
            showMessage("Oops... Server connection lost.");
        }
    }

    /**
     * Кнопка SEARCH.
     */
    @FXML
    void search(ActionEvent event) {
        String query = searchInput.getText();
        if (!query.isEmpty() && !query.isBlank()) {
            try {
                ClientController termRepository = new ClientController();
                List<Term> terms = termRepository.search(query);
                if (!terms.isEmpty()) {
                    termCards.getChildren().clear();
                    terms.forEach(this::createTermCard);
                } else {
                    showMessage("Nothing were found.");
                }
            } catch (IOException e) {
                showMessage("Oops... Server connection lost.");
            }
        }
    }

    /**
     * Кнопка сортировки по возрастанию
     */
    @FXML
    void sortAscending(ActionEvent event) {
        sortTerms(true);
    }

    /**
     * Отображает отсортированные термины.
     */
    private void sortTerms(boolean ascending) {
        ClientController termRepository = new ClientController();
        try {
            List<Term> terms = termRepository.sortByName(ascending);
            termCards.getChildren().clear();
            terms.forEach(this::createTermCard);
        } catch (IOException e) {
            showMessage("Oops... Server connection lost.");
        }
    }

    /**
     * Кнопка сортировки по убыванию
     */
    @FXML
    void sortDescending(ActionEvent event) {
        sortTerms(false);
    }

    /**
     * Сохранение термина в файл.
     */
    @FXML
    void saveTermAsFile(Term term) {
        File directory = chooseDirectory();
        String filename = String.format("/%s.txt", term.getName());
        File file = new File(directory.getAbsolutePath() + filename);
        try {
            if (file.createNewFile()) {
                Files.write(file.getAbsoluteFile().toPath(), term.getInformation().getBytes(), StandardOpenOption.APPEND);
                showAlertMessage("Term successfully saved.");
            } else {
                showAlertMessage("Such file already exist.");
            }
        } catch (IOException e) {
            showMessage("Can't save file.");
        }
    }

    private void showAlertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public File chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(null);
    }

    private void showMessage(String message) {
        Label label = new Label(message);
        label.setPrefWidth(700);
        label.setMaxWidth(Region.USE_PREF_SIZE);
        label.getStyleClass().add("message");
        termCards.getChildren().clear();
        termCards.getChildren().add(label);
    }

    @FXML
    public void onEnter(ActionEvent event) throws IOException {
        search(event);
    }

    private void createTermCard(Term term) {
        BorderPane cardHeader = new BorderPane();
        cardHeader.getStyleClass().add("term-header");
        Label termName = new Label(term.getName());
        termName.setMaxWidth(Double.MAX_VALUE);
        termName.setWrapText(true);
        termName.getStyleClass().add("term-label");
        cardHeader.setLeft(termName);

        Button downloadButton = creatDownloadButton();
        downloadButton.setOnAction(event -> {
            saveTermAsFile(term);
        });

        cardHeader.setRight(downloadButton);

        Label termInformation = new Label(term.getInformation());
        termInformation.setMaxWidth(Double.MAX_VALUE);
        termInformation.setWrapText(true);
        termInformation.getStyleClass().add("information");
        VBox termCard = new VBox(cardHeader, termInformation);
        termCard.setPrefWidth(700);
        termCard.setMaxWidth(Region.USE_PREF_SIZE);
        termCard.getStyleClass().add("term-card");
        termCards.getChildren().add(termCard);
    }

    private Button creatDownloadButton() {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M19,13 L19,18 C19,18.55 18.55,19 18,19 L6,19 C5.45,19 5,18.55 5,18 L5,13 C5,12.45 4.55,12 4,12 C3.45,12 3,12.45 3,13 L3,19 C3,20.1 3.9,21 5,21 L19,21 C20.1,21 21,20.1 21,19 L21,13 C21,12.45 20.55,12 20,12 C19.45,12 19,12.45 19,13 Z M13,12.67 L14.88,10.79 C15.27,10.4 15.9,10.4 16.29,10.79 C16.68,11.18 16.68,11.81 16.29,12.2 L12.7,15.79 C12.31,16.18 11.68,16.18 11.29,15.79 L7.7,12.2 C7.31,11.81 7.31,11.18 7.7,10.79 C8.09,10.4 8.72,10.4 9.11,10.79 L11,12.67 L11,4 C11,3.45 11.45,3 12,3 C12.55,3 13,3.45 13,4 L13,12.67 Z");
        Button button = new Button();
        button.getStyleClass().add("save-button");
        button.setGraphic(svgPath);

        return button;
    }
}
