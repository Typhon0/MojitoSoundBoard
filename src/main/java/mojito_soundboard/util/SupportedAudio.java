package mojito_soundboard.util;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

public enum SupportedAudio {

    mp3,
    flac,
    wav;


    public static boolean isAudioSupported(String filepath) {
        String extension = getFileExtension(filepath);
        return extension != null && contains(extension);
    }

    public static boolean contains(String test) {
        for (SupportedAudio c : SupportedAudio.values()) {
            if (c.name().toLowerCase().equals(test.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> listSupportedAudio(String prefix) {
        ArrayList<String> supportedAudioExtensions = new ArrayList<>();
        for (SupportedAudio extension : values()) {
            supportedAudioExtensions.add(prefix + extension.toString());
        }
        return supportedAudioExtensions;
    }

    public static ArrayList<String> listSupportedAudio() {
        return listSupportedAudio("");
    }

    /**
     * Returns the extension of file(without (.)) for example (ai.mp3) to (mp3)  and to lowercase (Mp3 to mp3)
     *
     * @param absolutePath The File absolute path
     * @return the File extension
     */
    public static String getFileExtension(String absolutePath) {
        return FilenameUtils.getExtension(absolutePath).toLowerCase();
    }
}
