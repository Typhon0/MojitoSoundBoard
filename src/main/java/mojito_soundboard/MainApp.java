package mojito_soundboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.AudioClip;
import mojito_soundboard.model.SoundBoard;

public class MainApp extends Application {

    private SoundBoard soundBoard;

    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        soundBoard = new SoundBoard();
        test();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));
        rootLayout = loader.load();
        MainController mainController = loader.getController();
        mainController.setMainApp(this);
        this.rootLayout.getStylesheets().add(getClass().getResource("/stylesheet/style.css").toExternalForm());
        primaryStage.setTitle("Mojito SoundBoard");
        primaryStage.setScene(new Scene(rootLayout, 600, 500));
        primaryStage.show();


    }


    public void test() {
        for (int i = 0; i < 200; i++) {
            soundBoard.getAudioClips().add(new AudioClip("test", "D:\\Windows\\Desktop\\piano2.wav", "ALT"));
        }
    }

    public AnchorPane getRootLayout() {
        return rootLayout;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public SoundBoard getSoundBoard() {
        return soundBoard;
    }
}
