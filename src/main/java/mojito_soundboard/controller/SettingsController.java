package mojito_soundboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javax.sound.sampled.*;
import java.io.IOException;

public class SettingsController {

    @FXML
    ComboBox<String> audioDeviceList;

    @FXML
    Button play;

    private Mixer device;

    @FXML
    public void initialize() {

        audioDeviceList.valueProperty().addListener((observable, oldValue, newValue) -> device = getMixer(newValue));

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer.Info info = mixerInfo[i];
            audioDeviceList.getItems().add(info.getName());
        }


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


    public void play(ActionEvent actionEvent) {

        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("piano2.wav"));
            Clip clip = AudioSystem.getClip(device.getMixerInfo());
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
