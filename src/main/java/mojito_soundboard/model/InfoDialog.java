package mojito_soundboard.model;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * @author Loïc Sculier aka typhon0
 */
public class InfoDialog extends Dialog {

    private String padName;
    private String filePath;
    private String shortcut;

    public InfoDialog() {

    }

    public InfoDialog(String padName, String filePath, String shortcut) {
        this.padName = padName;
        this.filePath = filePath;
        this.shortcut = shortcut;
        init();

    }

    /**
     * Create the dialog
     */
    private void init() {
        // Create the custom dialog.
        this.setTitle("File information");
        this.setHeaderText(null);
        this.initModality(Modality.APPLICATION_MODAL);


        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        grid.add(new Label("Pad name:"), 0, 0);
        grid.add(new Label(padName), 1, 0);
        grid.add(new Label("File path:"), 0, 1);
        grid.add(new Label(filePath), 1, 1);
        grid.add(new Label("Shortcut:"), 0, 2);
        grid.add(new Label(shortcut), 1, 2);


        this.getDialogPane().setContent(grid);


    }

    public String getShortcut() {
        return shortcut;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPadName() {
        return padName;
    }
}
