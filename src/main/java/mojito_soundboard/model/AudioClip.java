package mojito_soundboard.model;

import java.io.File;

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
    private File file;
    /**
     * Keyboard shortcutx
     */
    private String shortcut;

    public AudioClip(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public AudioClip(String name, File file, String shortcut) {
        this.name = name;
        this.file = file;
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
     * Get the file of the audio clip
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * Get the file path
     *
     * @return
     */
    public String getPath() {
        return file.getPath();
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
     * Set the audio file file
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Set the keyboard shortcut to play the audio
     *
     * @param shortcut
     */
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Override
    public String toString() {
        return "AudioClip{" +
                "name='" + name + '\'' +
                ", file=" + file +
                ", shortcut='" + shortcut + '\'' +
                '}';
    }
}
