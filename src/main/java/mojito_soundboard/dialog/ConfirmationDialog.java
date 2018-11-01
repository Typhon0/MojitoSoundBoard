package mojito_soundboard.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * @author Loïc Sculier aka typhon0
 */
public class ConfirmationDialog extends JFXDialog {

    private JFXButton ok;
    private JFXButton cancel;

    public ConfirmationDialog(StackPane dialogContainer, String text) {
        this.requestFocus();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Confirm"));
        content.setBody(new Text(text));

        ok = new JFXButton("OK");
        ok.setStyle("-fx-background-color: GRAY");
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        ok.setOnAction(event -> this.close());
        cancel = new JFXButton("Cancel");
        cancel.setStyle("-fx-background-color: GRAY");
        cancel.setButtonType(JFXButton.ButtonType.RAISED);
        cancel.setOnAction(event -> {
            this.getDialogContainer().toBack();
            this.close();
        });
        content.setActions(cancel, ok);

        this.setDialogContainer(dialogContainer);
        this.setContent(content);
        this.setTransitionType(DialogTransition.CENTER);

    }

    public JFXButton getOkButton() {
        return ok;
    }


    public JFXButton getCancelButton() {
        return cancel;
    }

}
