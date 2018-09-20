package mojito_soundboard.model;

import javafx.scene.control.ListCell;

/**
 * @author Loïc Sculier aka typhon0
 */
public class SoundBoardCell extends ListCell<SoundBoard> {

    @Override
    protected void updateItem(SoundBoard item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.getName());
            setGraphic(null);
        }
    }
}
