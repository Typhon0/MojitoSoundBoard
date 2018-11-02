package mojito_soundboard.model;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Loïc Sculier aka typhon0
 */
public class GlobalKeyListener implements NativeKeyListener {

    private NativeKeyEvent previousKeyEvent = null;

    public GlobalKeyListener() {
        // Get the logger for "org.jnativehook" and set the level
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_O && isControlModifierPressed(nativeKeyEvent) && isControlModifierPressed(previousKeyEvent)) {
        }
        previousKeyEvent = nativeKeyEvent;

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    /**
     * Detects if ShiftModifier is pressed
     *
     * @param e
     * @return True if yes , false if not
     */
    private boolean isShiftModifierPressed(NativeKeyEvent e) {
        return e.getModifiers() == NativeKeyEvent.SHIFT_L_MASK || e.getModifiers() == NativeKeyEvent.SHIFT_R_MASK || e.getModifiers() == NativeKeyEvent.SHIFT_MASK
                || e.getModifiers() == NativeKeyEvent.VC_SHIFT;
    }

    /**
     * Detects if ControlModifier is pressed
     *
     * @param e
     * @return True if yes , false if not
     */
    private boolean isControlModifierPressed(NativeKeyEvent e) {
        return (e.getModifiers() == NativeKeyEvent.CTRL_L_MASK || e.getModifiers() == NativeKeyEvent.CTRL_R_MASK || e.getModifiers() == NativeKeyEvent.CTRL_MASK
                || e.getModifiers() == NativeKeyEvent.VC_CONTROL);
    }
}
