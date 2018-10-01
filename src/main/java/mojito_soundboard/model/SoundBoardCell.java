package mojito_soundboard.model;

import com.jfoenix.controls.JFXListCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.util.DBHelper;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
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

        Label name = new Label(item.getName());
        Button del = new Button();
        del.setOnAction(event -> {
            mainController.deleteSoundboard(item);
        });
        StackPane.setAlignment(name, Pos.CENTER_LEFT);
        StackPane.setAlignment(del, Pos.CENTER_RIGHT);
        FontIcon delGraphic = new FontIcon(Ionicons4IOS.REMOVE_CIRCLE);
        delGraphic.setIconSize(20);
        del.setGraphic(delGraphic);
        del.setVisible(false);
        del.visibleProperty().bind(mainController.editModeProperty());
        del.getStyleClass().add("controlButton");


        stackPane.getChildren().addAll(name, del);


        return stackPane;

    }
}
