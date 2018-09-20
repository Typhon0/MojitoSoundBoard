package mojito_soundboard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.AudioClip;
import mojito_soundboard.model.SoundBoard;

import java.io.File;
import java.util.ArrayList;

public class MainApp extends Application {

    private ObservableList<SoundBoard> soundBoards;

    private AnchorPane rootLayout;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        soundBoards = FXCollections.observableArrayList();
        test();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));
        rootLayout = loader.load();
        MainController mainController = loader.getController();
        mainController.setMainApp(this);
        this.rootLayout.getStylesheets().add(getClass().getResource("/stylesheet/style.css").toExternalForm());
        this.primaryStage.setTitle("Mojito SoundBoard");
        this.primaryStage.setScene(new Scene(rootLayout, 600, 500));
        this.primaryStage.setMinWidth(600);
        this.primaryStage.show();


    }


    public void test() {
        SoundBoard soundBoard = new SoundBoard("Soundboard 1");
        for (int i = 0; i < 200; i++) {
            soundBoard.getAudioClips().add(new AudioClip("test 1", new File("D:\\Windows\\Desktop\\piano2.wav"), "ALT"));
        }

        SoundBoard soundBoard2 = new SoundBoard("Soundboard 2");
        for (int i = 0; i < 200; i++) {
            soundBoard2.getAudioClips().add(new AudioClip("test 2", new File("D:\\Windows\\Desktop\\piano2.wav"), "ALT"));
        }

        soundBoards.add(soundBoard);
        soundBoards.add(soundBoard2);
    }

    public AnchorPane getRootLayout() {
        return rootLayout;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<SoundBoard> getSoundBoards() {
        return soundBoards;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
