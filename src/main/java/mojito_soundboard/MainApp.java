package mojito_soundboard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mojito_soundboard.controller.MainController;
import mojito_soundboard.model.SoundBoard;
import mojito_soundboard.util.DBHelper;
import mojito_soundboard.util.stream.StreamPlayer;

import java.util.prefs.Preferences;

public class MainApp extends Application {

    private ObservableList<SoundBoard> soundBoards;

    private AnchorPane rootLayout;

    private Stage primaryStage;

    private StreamPlayer streamPlayer;

    private Preferences preferences;

    @Override
    public void start(Stage primaryStage) throws Exception {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        streamPlayer = new StreamPlayer();
        this.primaryStage = primaryStage;
        soundBoards = FXCollections.observableArrayList();
        DBHelper.initDB(System.getProperty("user.dir"));
        soundBoards.setAll(DBHelper.loadSoundboards());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));
        rootLayout = loader.load();
        MainController mainController = loader.getController();
        mainController.setMainApp(this);
        this.primaryStage.setTitle("Mojito SoundBoard");
        this.primaryStage.setScene(new Scene(rootLayout, 600, 500));
        this.primaryStage.setMinWidth(600);
        this.primaryStage.show();


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

    public StreamPlayer getStreamPlayer() {
        return streamPlayer;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public void stop() throws Exception {
        super.stop();

    }
}
