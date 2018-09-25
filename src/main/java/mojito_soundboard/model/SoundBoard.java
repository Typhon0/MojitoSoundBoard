package mojito_soundboard.model;

import javax.sound.sampled.AudioSystem;
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

    /**
     * Soundboard id
     */
    private int id;

    public ArrayList<AudioClip> getAudioClips() {
        return audioClips;
    }

    public SoundBoard(String name) {
        this.name = name;
        audioClips = new ArrayList<>();

    }

    public SoundBoard(int id, String name) {
        this.id = id;
        this.name = name;
        audioClips = new ArrayList<>();


    }

    public SoundBoard(int id, String name, String mixer) {
        this.id = id;
        this.name = name;
        this.mixer = getMixer(mixer);
        audioClips = new ArrayList<>();


    }


    public Mixer getMixer(String deviceName) {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer.Info info = mixerInfo[i];
            if (info.getName().equals(deviceName)) {
                return AudioSystem.getMixer(info);
            }
        }

        return null;
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
