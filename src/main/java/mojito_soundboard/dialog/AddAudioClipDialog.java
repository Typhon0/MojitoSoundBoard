package mojito_soundboard.dialog;

import com.jfoenix.controls.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.AudioClip;

import java.io.File;


/**
 * @author Loïc Sculier aka typhon0
 */
public class AddAudioClipDialog extends JFXDialog {

    /**
     * Constructor
     *
     * @param mainController reference to the main controller
     */
    public AddAudioClipDialog(MainController mainController) {
        JFXDialogLayout content = new JFXDialogLayout();
        GridPane grid = new GridPane();


        JFXTextField name = new JFXTextField();
        JFXTextField file = new JFXTextField();
        JFXTextField shortcutField = new JFXTextField();
        JFXColorPicker jfxColorPicker = new JFXColorPicker();

        name.setPromptText("Name");
        file.setPromptText("File");
        file.setOnMouseClicked(event -> {
            file.setText(mainController.openFileChooser().getPath());
        });
        shortcutField.setPromptText("Shortcut");
        grid.add(new Label("Audio clip name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Soundboard name:"), 0, 1);
        grid.add(file, 1, 1);
        grid.add(new Label("Shortcut:"), 0, 2);
        grid.add(shortcutField, 1, 2);
        Label colorL = new Label("Color:");

        GridPane.setMargin(colorL, new Insets(10, 0, 0, 0));
        grid.add(colorL, 0, 3);

        GridPane.setMargin(jfxColorPicker, new Insets(10, 0, 0, 0));
        grid.add(jfxColorPicker, 1, 3);
        grid.setHgap(10);
        content.setBody(grid);
        JFXButton ok = new JFXButton("Ok");
        ok.setStyle("-fx-background-color: GRAY");
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        ok.setOnAction(event -> {
            AudioClip newAudioClip = null;
            if (!name.getText().isEmpty() && !file.getText().isEmpty()) {
                if (shortcutField.getText().isEmpty()) {
                    newAudioClip = new AudioClip(name.getText(), new File(file.getText()), shortcutField.getText(), jfxColorPicker.getValue());
                } else {
                    newAudioClip = new AudioClip(name.getText(), new File(file.getText()), jfxColorPicker.getValue());
                }
            }
            mainController.addAudioClip(newAudioClip);
        });
        content.setActions(ok);

        this.setDialogContainer(mainController.getDialogstackpane());
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
    }


}
