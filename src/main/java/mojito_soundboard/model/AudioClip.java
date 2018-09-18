package mojito_soundboard.model;

/**
 * @author Loïc Sculier aka typhon0
 */
public class AudioClip {

    /**
     * Name of the audio clip
     */
    private String name;
    /**
     * Path to the audio file
     */
    private String path;
    /**
     * Keyboard shortcut
     */
    private String shortcut;

    public AudioClip(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public AudioClip(String name, String path, String shortcut) {
        this.name = name;
        this.path = path;
        this.shortcut = shortcut;
    }

    /**
     * Get the keyboard shortcut to play the audio
     *
     * @return
     */
    public String getShortcut() {
        return shortcut;
    }

    /**
     * Get the path of the audio clip
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the name of the audio clip
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the audio clip
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the audio file path
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set the keyboard shortcut to play the audio
     *
     * @param shortcut
     */
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
}
