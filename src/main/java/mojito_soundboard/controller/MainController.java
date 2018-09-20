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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import mojito_soundboard.MainApp;
import mojito_soundboard.model.AudioClip;
import mojito_soundboard.model.InfoDialog;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
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
    private ArrayList<HBox> editContainers;

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
     * Information dialog about the file
     */
    InfoDialog infoDialog;

    /**
     * Called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        editMode = new SimpleBooleanProperty(false);
        board = new ArrayList<>();
        editContainers = new ArrayList<>();
        infoDialog = new InfoDialog();

        Platform.runLater(() -> {
            for (AudioClip audioClip : mainApp.getSoundBoard().getAudioClips()) {

                board.add(createButton(audioClip, 120));
            }
            grid.getChildren().addAll(board);


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
    public Button createButton(AudioClip audioClip, int buttonsize) {
        Button button = new Button();

        button.setOnAction(event -> {
            // Call play()
        });

        //Edit container
        HBox edit = new HBox();
        edit.setAlignment(Pos.BOTTOM_LEFT);
        edit.setVisible(false);
        Button file = new Button();

        FontIcon filegraphic = new FontIcon(Ionicons4IOS.DOCUMENT);
        filegraphic.setIconSize(24);
        file.setGraphic(filegraphic);
        file.getStyleClass().add("controlButton");
        file.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            if (audiofile != null) {
                audioClip.setFile(audiofile);
            } else {
                audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            }

        });

        Button repeat = new Button();
        FontIcon repeatgraphic = new FontIcon(Ionicons4IOS.REPEAT);
        repeatgraphic.setIconSize(24);
        repeat.setGraphic(repeatgraphic);
        repeat.getStyleClass().add("controlButton");

        Button info = new Button();
        FontIcon infographic = new FontIcon(Ionicons4IOS.INFORMATION_CIRCLE);
        infographic.setIconSize(24);
        info.setGraphic(infographic);
        info.getStyleClass().add("controlButton");
        info.setOnAction(event -> {
            infoDialog = new InfoDialog(audioClip.getName(), audioClip.getPath(), audioClip.getShortcut());
            infoDialog.showAndWait();

        });


        edit.getChildren().addAll(file, repeat, info);
        editContainers.add(edit);
        button.setGraphic(edit);

        button.setMaxSize(buttonsize, buttonsize);
        button.setPrefWidth(buttonsize);
        button.setContentDisplay(ContentDisplay.BOTTOM);
        button.setAlignment(Pos.BOTTOM_LEFT);
        button.setPrefHeight(buttonsize);
        button.setMinHeight(buttonsize);
        button.setMinWidth(buttonsize);
        button.setCache(true);
        button.setText(audioClip.getName());


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
        for (HBox edit_container : editContainers) {
            edit_container.setVisible(true);
            setEditMode(true);
        }
    }

    /**
     * Disable edit mode
     */
    public void disableEditMode() {
        for (HBox edit_container : editContainers) {
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
