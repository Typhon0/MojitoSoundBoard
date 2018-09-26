package mojito_soundboard.util;

import org.apache.commons.io.FilenameUtils;

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

    /**
     * Returns the extension of file(without (.)) for example <b>(ai.mp3)->(mp3)</b> and to lowercase (Mp3 -> mp3)
     *
     * @param absolutePath The File absolute path
     * @return the File extension
     */
    public static String getFileExtension(String absolutePath) {
        return FilenameUtils.getExtension(absolutePath).toLowerCase();
    }
}
