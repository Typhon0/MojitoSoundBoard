package mojito_soundboard.model;

import com.jfoenix.controls.JFXListCell;

/**
 * @author Loïc Sculier aka typhon0
 */
public class SoundBoardCell extends JFXListCell<SoundBoard> {

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
