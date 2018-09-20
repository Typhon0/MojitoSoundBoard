package mojito_soundboard.model;

import javax.sound.sampled.Mixer;
import java.util.ArrayList;

/**
 * @author Loïc Sculier aka typhon0
 */
public class SoundBoard {

    /**
     * Arraylist of audio clips
     */
    private ArrayList<AudioClip> audioClips;

    private Mixer mixer;

    /**
     * Soundboard name
     */
    private String name;

    public ArrayList<AudioClip> getAudioClips() {
        return audioClips;
    }

    public SoundBoard(String name) {
        this.name = name;
        audioClips = new ArrayList<>();

    }

    public void setMixer(Mixer mixer) {
        this.mixer = mixer;
    }

    public Mixer getMixer() {
        return mixer;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SoundBoard{" +
                "audioClips=" + audioClips +
                ", mixer=" + mixer +
                ", name='" + name + '\'' +
                '}';
    }
}
