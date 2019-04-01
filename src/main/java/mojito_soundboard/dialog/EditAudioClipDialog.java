package mojito_soundboard.dialog;

import com.jfoenix.controls.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.AudioClip;

/**
 * @author Loïc Sculier aka typhon0
 */
public class EditAudioClipDialog extends JFXDialog {


    /**
     * Constructor
     *
     * @param mainController reference to the main controller
     * @param audioClip      audio clip to edit
     */
    public EditAudioClipDialog(MainController mainController, AudioClip audioClip) {
        JFXDialogLayout content = new JFXDialogLayout();
        GridPane grid = new GridPane();
        JFXTextField name = new JFXTextField();
        JFXTextField file = new JFXTextField();
        JFXTextField shortcutField = new JFXTextField();
        JFXColorPicker jfxColorPicker = new JFXColorPicker();

        //Load audio clip
        name.setText(audioClip.getName());
        file.setText(audioClip.getPath());
        shortcutField.setText(audioClip.getShortcut());
        jfxColorPicker.setValue(audioClip.getColor());

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
            AudioClip audioClipEdited = new AudioClip(audioClip);
            if (!name.getText().equals(audioClip.getName())) {
                audioClipEdited.setName(name.getText());
            } else if (!file.getText().equals(audioClip.getPath())) {
                audioClipEdited.setFile(file.getText());
            } else if (audioClip.getShortcut() != null && !shortcutField.getText().equals(audioClip.getShortcut())) {
                audioClipEdited.setShortcut(shortcutField.getText());
            }
            audioClipEdited.setColor(jfxColorPicker.getValue());
            //Edit audio clip
            mainController.editAudioClip(audioClipEdited);
        });
        JFXButton cancel = new JFXButton("Cancel");
        cancel.setStyle("-fx-background-color: GRAY");
        cancel.setButtonType(JFXButton.ButtonType.RAISED);
        cancel.setOnAction(e -> this.close());
        content.setActions(cancel,ok);
        this.setDialogContainer(mainController.getDialogstackpane());
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
    }


}
