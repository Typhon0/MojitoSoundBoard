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
import mojito_soundboard.util.DBHelper;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(SettingsController.class.getName());

    public SettingsController() {}

    @FXML
    public void initialize() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info info: mixerInfo){
            audioDeviceList.getItems().add(info.getName());
        }

        copyrightLabel.setText("© 2019 Typhon0 and LenoirRemi All Rights Reserved");

        Platform.runLater(() -> {
            audioDeviceList.setValue(mainApp.getPreferences().get(MIXER_KEY, ""));
            audioDeviceList.valueProperty().addListener((observable, oldValue, newValue) -> {
                mainApp.getPreferences().put(MIXER_KEY, newValue);
                mainApp.getStreamPlayer().setMixerName(newValue);
                DBHelper.editMixer(mainApp.getMainController().getCurrentSoundboard(), newValue);
                logger.info(() -> "Mixer changed to : " + newValue);
            });
        });
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
