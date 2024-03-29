package mojito_soundboard.model;

import javafx.scene.paint.Color;

import java.io.File;

/**
 * @author Loïc Sculier aka typhon0
 */
public class AudioClip {

    /**
     * Database audio clip id
     * need to subtract 1 for the ArrayList index
     */
    private int id;

    /**
     * Name of the audio clip
     */
    private String name;
    /**
     * Path to the audio file
     */
    private File file;
    /**
     * Keyboard shortcut
     */
    private String shortcut;

    private Color color;

    /**
     * Database Soundboard id
     * need to subtract 1 for the ArrayList index
     */
    private int idSoundboard;

    public AudioClip(AudioClip audioClip) {
        this.id = audioClip.getId();
        this.name = audioClip.getName();
        this.file = audioClip.getFile();
        this.shortcut = audioClip.getShortcut();
        this.color = audioClip.getColor();
        this.idSoundboard = audioClip.getIdSoundboard();
    }

    public AudioClip(int id, String name, File file, String shortcut, Color color, int idSoundboard) {
        this.id = id;
        this.idSoundboard = idSoundboard;
        this.name = name;
        this.file = file;
        this.shortcut = shortcut;
        this.color = color;
    }


    public AudioClip(String name, File file, Color color) {
        this.shortcut = "";
        this.name = name;
        this.file = file;
        this.color = color;
    }

    public AudioClip(String name, File file, String shortcut, Color color) {
        this.name = name;
        this.file = file;
        this.shortcut = shortcut;
        this.color = color;
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
     * Set the audio file file
     *
     * @param filepath
     */
    public void setFile(String filepath) {
        this.file = new File(filepath);
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", shortcut='" + shortcut + '\'' +
                ", color=" + color +
                ", idSoundboard=" + idSoundboard +
                '}';
    }

    /**
     * Get the database ID of the audio clip
     *
     * @return the database audio clip ID
     */
    public int getId() {
        return id;
    }


    /**
     * Get the database ID of the soundboard of the audio clip
     *
     * @return the database soundboard ID of the audio clip
     */
    public int getIdSoundboard() {
        return idSoundboard;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean sameID(AudioClip audioClip) {
        if (audioClip.getId() == this.getId() && audioClip.getIdSoundboard() == this.getIdSoundboard()) {
            return true;
        }
        return false;
    }
}
