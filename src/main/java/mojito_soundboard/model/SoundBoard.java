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


    public ArrayList<AudioClip> getAudioClips() {
        return audioClips;
    }

    public SoundBoard(){

    }

    public void setMixer(Mixer mixer) {
        this.mixer = mixer;
    }

    public Mixer getMixer() {
        return mixer;
    }
}
