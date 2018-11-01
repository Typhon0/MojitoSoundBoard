package mojito_soundboard.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.SoundBoard;

/**
 * @author Loïc Sculier aka typhon0
 */
public class EditSoundboardDialog extends JFXDialog {

    /**
     * Constructor
     *
     * @param mainController reference to the main controller
     * @param soundBoard     soundboard to edit
     */
    public EditSoundboardDialog(MainController mainController, SoundBoard soundBoard) {
        JFXDialogLayout content = new JFXDialogLayout();

        GridPane grid = new GridPane();
        JFXTextField name = new JFXTextField();
        name.setPromptText("Name");
        name.setText(soundBoard.getName());
        grid.add(new Label("Soundboard name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.setHgap(10);
        content.setBody(grid);
        JFXButton ok = new JFXButton("Ok");
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        ok.setStyle("-fx-background-color: GRAY");
        ok.setOnAction(event -> {
            soundBoard.setName(name.getText());
            mainController.editSoundboard(soundBoard);
        });
        content.setActions(ok);
        this.setDialogContainer(mainController.getDialogstackpane());
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);
    }


}
