package mojito_soundboard.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Duration;
import mojito_soundboard.MainApp;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.util.SupportedAudio;
import mojito_soundboard.util.stream.StreamPlayerException;
import sun.applet.Main;

import java.io.File;

/**
 * @author Loïc Sculier aka typhon0
 */
public class PlayService extends Service<Boolean> {
    private final MainApp mainApp;
    private final MainController mainController;
    private volatile boolean locked;
    private File currentAudioClip;

    public PlayService(MainApp mainApp, MainController mainController) {
        this.mainApp = mainApp;
        this.mainController = mainController;
    }

    /**
     * Start the Service.
     *
     * @param fileAbsolutePath The path of the audio
     * @param secondsToSkip
     */
    public void startPlayService(String fileAbsolutePath, int secondsToSkip) {

        //First Security Check

        if (locked || isRunning() || fileAbsolutePath == null) {
            return;
        }

        //  the audio file
        currentAudioClip = new File(fileAbsolutePath);
        // Restart the Service
        restart();

        // lock the Service
        locked = true;

    }

    /**
     * When the Service is done.
     */
    private void done() {

        try {
            mainApp.getStreamPlayer().play();
            mainApp.getStreamPlayer().resume();
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
        locked = false;

    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                try {
                    //stop previous
                    mainApp.getStreamPlayer().stop();

                    //Open the audio
                    updateMessage("Opening ...");
                    System.out.println(currentAudioClip);
                    mainApp.getStreamPlayer().open(currentAudioClip);

                    // ----------------------- Play the Audio
                    updateMessage("Starting ...");
                    mainApp.getStreamPlayer().play();
                    mainApp.getStreamPlayer().pause();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {

                    // Print the current audio file path
                    System.out.println("Current audio path is ...:" + currentAudioClip.getPath());

                }

                return true;


            }
        };
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println("Play service succeed");
        done();
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.println("Play service failed");
        done();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        System.out.println("Play service cancelled");

    }
}