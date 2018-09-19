package mojito_soundboard.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mojito_soundboard.MainApp;
import mojito_soundboard.model.AudioClip;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {

    /**
     * Soundboard grid
     */
    @FXML
    public FlowPane grid;

    @FXML
    public AnchorPane root;

    /**
     * Reference to the MainApp class
     */
    private MainApp mainApp;

    /**
     * List of buttons
     */
    private ArrayList<Button> board;


    @FXML
    Parent settings;

    @FXML
    public void initialize() {
        board = new ArrayList<>();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (AudioClip audioClip : mainApp.getSoundBoard().getAudioClips()) {

                    board.add(createButton(audioClip));
                }
                grid.getChildren().addAll(board);


            }
        });
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/settings.fxml"));
            settings = loader.load();
            SettingsController settingsController = loader.getController();
            settingsController.setMainApp(mainApp);
            root.getChildren().add(settings);
            settings.toBack();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the MainApp
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Get the MainApp
     *
     * @return MainApp
     */
    public MainApp getMainApp() {
        return mainApp;
    }

    public void addBtn(ActionEvent actionEvent) {
    }

    public void settingsBtn(ActionEvent actionEvent) {

        settings.toFront();
        settings.requestFocus();
        Timeline t =
                new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(settings.opacityProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))

                        ),
                        new KeyFrame(Duration.millis(200),
                                new KeyValue(settings.opacityProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                        )
                );
        t.play();
        settings.setVisible(true);


    }

    public void play(String path) {

        //TODO Play
    }

    private void stop() {

        //TODO stop
    }

    public Button createButton(AudioClip audioClip) {
        Button button = new Button();
        button.setOnAction(event -> {
            // Call play()
        });
        VBox edit = new VBox();
        edit.getChildren().addAll(new Button("edit"), new Button("loop"));
        button.setGraphic(edit);
        button.setMaxSize(100, 100);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setMinHeight(100);
        button.setMaxHeight(100);
        return button;
    }
}
