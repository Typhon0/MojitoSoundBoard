package mojito_soundboard.controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mojito_soundboard.MainApp;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class SettingsController {

    @FXML
    ComboBox<String> audioDeviceList;

    @FXML
    StackPane settings;

    @FXML
    Button exit;

    @FXML
    Label copyrightLabel;

    private MainApp mainApp;

    private final String MIXER_KEY = "MIXER";

    public SettingsController() {

    }

    @FXML
    public void initialize() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer.Info info = mixerInfo[i];
            audioDeviceList.getItems().add(info.getName());
        }

        Platform.runLater(() -> {
            audioDeviceList.setValue(mainApp.getPreferences().get(MIXER_KEY, ""));
            audioDeviceList.valueProperty().addListener((observable, oldValue, newValue) -> {
                mainApp.getPreferences().put(MIXER_KEY, newValue);
                mainApp.getStreamPlayer().setMixerName(newValue);
            });
        });
        copyrightLabel.setText("© 2019 Typhon0 and LenoirRemi All Rights Reserved");
    }


    public Mixer getMixer(String deviceName) {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer.Info info = mixerInfo[i];
            if (info.getName().equals(deviceName)) {
                return AudioSystem.getMixer(info);
            }
        }

        return null;
    }


    public void exit(ActionEvent actionEvent) {
        Timeline t =
                new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(settings.opacityProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))

                        ),
                        new KeyFrame(Duration.millis(200),
                                new KeyValue(settings.opacityProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.visibleProperty(), false, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                        )
                );
        t.play();


    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
