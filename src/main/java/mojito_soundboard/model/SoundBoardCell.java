package mojito_soundboard.model;

import com.jfoenix.controls.JFXListCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.util.DBHelper;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
import org.kordamp.ikonli.ionicons4.Ionicons4Material;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Loïc Sculier aka typhon0
 */
public class SoundBoardCell extends JFXListCell<SoundBoard> {

    MainController mainController;

    public SoundBoardCell(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    protected void updateItem(SoundBoard item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            setGraphic(createGraphic(item));
        }
    }


    private Node createGraphic(SoundBoard item) {

        StackPane stackPane = new StackPane();
        stackPane.setPrefWidth(200);

        HBox hBox = new HBox();

        Label name = new Label();
        name.textProperty().bind(item.nameProperty());
        Button del = new Button();
        del.setOnAction(event -> {
            mainController.deleteSoundboard(item);
        });
        Button edit = new Button();
        edit.setOnAction(event -> {
            mainController.editSoundboardDialog(item);
        });

        FontIcon delGraphic = new FontIcon(Ionicons4IOS.REMOVE_CIRCLE);
        delGraphic.setIconSize(20);
        del.setGraphic(delGraphic);
        del.getStyleClass().add("controlButton");
        FontIcon editGraphic = new FontIcon(Ionicons4Material.CREATE);
        editGraphic.setIconSize(20);
        edit.setGraphic(editGraphic);
        edit.getStyleClass().add("controlButton");
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.visibleProperty().bind(mainController.editModeProperty());
        hBox.getChildren().addAll(edit, del);
        StackPane.setAlignment(name, Pos.CENTER_LEFT);
        stackPane.getChildren().addAll(name, hBox);


        return stackPane;

    }
}
