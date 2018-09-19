package mojito_soundboard.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    /**
     * Root anchorpane
     */
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

    /**
     * List of edit containers
     */
    private ArrayList<VBox> editContainers;

    /**
     * Edit mode boolean property
     */
    private BooleanProperty editMode;

    /**
     * Setting container
     */
    @FXML
    Parent settings;

    /**
     * Called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        editMode = new SimpleBooleanProperty(false);
        board = new ArrayList<>();
        editContainers = new ArrayList<>();
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
     * Handle the add button event
     * Add a button to the soundboard grid
     *
     * @param actionEvent
     */
    public void addBtn(ActionEvent actionEvent) {
    }

    /**
     * Handle the settings button event
     * Open the settings panel
     *
     * @param actionEvent
     */
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

    /**
     * Play the audio clip
     *
     * @param path
     */
    public void play(String path) {

        //TODO Play
    }

    /**
     * Stop playing the audio clip
     */
    private void stop() {

        //TODO stop
    }

    /**
     * Create a soundboard button
     *
     * @param audioClip the audio clip linked to the button
     * @return a Button
     */
    public Button createButton(AudioClip audioClip) {
        Button button = new Button();
        button.setOnAction(event -> {
            // Call play()
        });
        //Edit container
        VBox edit = new VBox();
        edit.setVisible(false);
        edit.getChildren().addAll(new Button("edit"), new Button("loop"));
        editContainers.add(edit);
        button.setGraphic(edit);


        button.setMaxSize(100, 100);
        button.setPrefWidth(100);
        button.setPrefHeight(100);
        button.setMinHeight(100);
        button.setMaxHeight(100);
        button.setCache(true);
        return button;
    }

    /**
     * Handle the button event to enable or disable edit mode
     *
     * @param actionEvent
     */
    public void editBtn(ActionEvent actionEvent) {
        if (editMode.getValue()) {
            disableEditMode();
        } else {
            enableEditMode();
        }
    }

    /**
     * Enable edit mode
     */
    public void enableEditMode() {
        for (VBox edit_container : editContainers) {
            edit_container.setVisible(true);
            setEditMode(true);
        }
    }

    /**
     * Disable edit mode
     */
    public void disableEditMode() {
        for (VBox edit_container : editContainers) {
            edit_container.setVisible(false);
            setEditMode(false);
        }
    }

    public boolean isEditMode() {
        return editMode.get();
    }

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode.set(editMode);


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
}
