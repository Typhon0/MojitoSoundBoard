package mojito_soundboard.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import mojito_soundboard.MainApp;

import javax.sound.sampled.*;
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
        for (int i = 0; i < 20; i++) {
            Button button = new Button();
            button.setMaxSize(100, 100);
            button.setPrefWidth(100);
            button.setPrefHeight(100);
            button.setMinHeight(100);
            button.setMaxHeight(100);
            board.add(button);
        }

        grid.getChildren().addAll(board);
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

    public void play() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(MainApp.class.getResourceAsStream("audio"));
            Clip clip = AudioSystem.getClip(mainApp.getSoundBoard().getMixer().getMixerInfo());
            clip.open(inputStream);
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
