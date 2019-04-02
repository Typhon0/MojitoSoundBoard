package mojito_soundboard.dialog;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.AudioClip;
import mojito_soundboard.util.SupportedAudio;

import java.io.File;


/**
 * @author Loïc Sculier aka typhon0
 */
public class AddAudioClipDialog extends JFXDialog {

    private ObservableList<String> keys = FXCollections.observableArrayList();

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
            File fileObj = mainController.openFileChooser();
            file.setText(fileObj != null ? fileObj.getPath() : "");
        });
        shortcutField.setPromptText("Shortcut");
        //shortcutField.setOnKeyPressed(event -> System.out.println(event));
        shortcutField.setOnKeyPressed(event -> {
            //keys.add(event.getCode().getName());
            //shortcutField.setText(event.getCode().getName());
           // System.out.println(KeyStroke.getKeyStroke(event.getCode(), event.));
        });
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
            File tempFile = new File(file.getText());
            if (!name.getText().isEmpty() && !file.getText().isEmpty() && tempFile.exists() && SupportedAudio.isAudioSupported(tempFile.getPath())) {
                if (shortcutField.getText().isEmpty()) {
                    newAudioClip = new AudioClip(name.getText(), new File(file.getText()), shortcutField.getText(), jfxColorPicker.getValue());
                } else {
                    newAudioClip = new AudioClip(name.getText(), new File(file.getText()), jfxColorPicker.getValue());
                }
                mainController.addAudioClip(newAudioClip);
            } else {
                if(name.getText().isEmpty()) {
                    name.setFocusColor(new Color(1, 0, 0,1));
                    name.requestFocus();
                }else if(file.getText().isEmpty() || !tempFile.exists() || !SupportedAudio.isAudioSupported(tempFile.getPath())){
                    file.setFocusColor(new Color(1, 0, 0,1));
                    file.requestFocus();
                }
            }
        });
        name.setOnKeyReleased(event -> {
            if(name.getText().length() > 0) {
                name.setFocusColor(new Color(0.251,0.349,0.663,1));
            }else {
                name.setFocusColor(new Color(1,0,0,1));
            }
        });
        file.setOnKeyReleased(event -> {
            if(file.getText().length() > 0) {
                file.setFocusColor(new Color(0.251,0.349,0.663,1));
            }else{
                file.setFocusColor(new Color(1,0,0,1));
            }
        });

        JFXButton cancel = new JFXButton("Cancel");
        cancel.setStyle("-fx-background-color: GRAY");
        cancel.setButtonType(JFXButton.ButtonType.RAISED);
        cancel.setOnAction(e -> this.close());
        content.setActions(cancel,ok);

        this.setDialogContainer(mainController.getDialogstackpane());
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
        this.setOnDialogOpened(event -> {
            name.requestFocus();
        });
    }


}
