package mojito_soundboard.controller;

import com.jfoenix.controls.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import mojito_soundboard.MainApp;
import mojito_soundboard.dialog.*;
import mojito_soundboard.model.*;
import mojito_soundboard.util.DBHelper;
import mojito_soundboard.util.SupportedAudio;
import mojito_soundboard.util.stream.StreamPlayerEvent;
import mojito_soundboard.util.stream.StreamPlayerException;
import mojito_soundboard.util.stream.StreamPlayerListener;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
import org.kordamp.ikonli.ionicons4.Ionicons4Material;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainController implements StreamPlayerListener {

    /**
     * Soundboard grid
     */
    @FXML
    public FlowPane grid;

    /**
     * Root anchorpane
     */
    @FXML
    public AnchorPane root;

    /**
     * Volume slider
     */
    @FXML
    public JFXSlider volumeSlider;

    /**
     * Reference to the MainApp class
     */
    private MainApp mainApp;

    /**
     * The hamburger button
     */
    @FXML
    JFXHamburger hamburger;

    /**
     * Stackpane where to show the dialog
     */
    @FXML
    StackPane dialogstackpane;

    /**
     * The drawer list view
     */
    @FXML
    JFXListView<SoundBoard> listview;

    /**
     * List of buttons
     */
    private ObservableList<Button> board;

    /**
     * List of edit containers
     */
    private ArrayList<HBox> editContainers;

    /**
     * Edit mode boolean property
     */
    private BooleanProperty editMode;

    /**
     * Drawer with list of soundboard
     */
    @FXML
    private JFXDrawer drawer;

    /**
     * Label to display current soundboard
     */
    @FXML
    private Label soundboardLabel;

    /**
     * Add audio clip button
     */
    @FXML
    private Button addAudioClipBtn;

    /**
     * Setting container
     */
    @FXML
    public Parent settings;

    /**
     * The scrollpane
     */
    @FXML
    ScrollPane scrollpane;

    /**
     * JFX dialog
     */
    private JFXDialog jfxDialog;

    /**
     * Save current index of the selected soundboard
     */
    private int currentSoundboardIndex;

    /**
     * Service to play a song
     */
    private PlayService playService;

    /**
     * Called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        editMode = new SimpleBooleanProperty(false);
        board = FXCollections.observableArrayList();
        editContainers = new ArrayList<>();

        JFXScrollPane.smoothScrolling(scrollpane);

        //Hamburger initialization
        HamburgerBasicCloseTransition burgerTask = new HamburgerBasicCloseTransition(hamburger);
        burgerTask.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();
        });


        Platform.runLater(() -> {
            playService = new PlayService(mainApp, this);

            listview.setItems(mainApp.getSoundBoards());
            listview.setCellFactory(param -> new SoundBoardCell(this));
            if (mainApp.getSoundBoards().size() > 0) {
                loadSoundboard(0);
            }
            listview.getSelectionModel().selectedIndexProperty().addListener(event -> {
                loadSoundboard(listview.getSelectionModel().getSelectedIndex());
                currentSoundboardIndex = listview.getSelectionModel().getSelectedIndex();
            });

            if (mainApp.getSoundBoards().size() <= 0) {
                addAudioClipBtn.setDisable(true);
            }
            loadSettings();
            volumeControl();
        });


    }

    /**
     * Load settings UI
     */
    public void loadSettings() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/settings.fxml"));
            settings = loader.load();
            SettingsController settingsController = loader.getController();
            settingsController.setMainApp(mainApp);
            root.getChildren().add(settings);
            settings.toBack();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Handle the settings button event
     * Open the settings panel
     *
     * @param actionEvent
     */
    public void settingsBtn(ActionEvent actionEvent) {

        settings.toFront();
        settings.requestFocus();
        Timeline t =
                new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(settings.opacityProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1.5, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))

                        ),
                        new KeyFrame(Duration.millis(200),
                                new KeyValue(settings.opacityProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleXProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                                new KeyValue(settings.scaleYProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                        )
                );
        t.play();
        settings.setVisible(true);


    }

    /**
     * Play the  song.
     *
     * @param absolutePath The absolute path of the file
     */
    public void playSong(String absolutePath) {

        playService.startPlayService(absolutePath, 0);

    }

    /**
     * Starts the player
     */
    public void play() {
        try {
            mainApp.getStreamPlayer().play();
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resume the player.
     */
    public void resume() {
        mainApp.getStreamPlayer().resume();
    }

    /**
     * Pause the player.
     */
    public void pause() {
        mainApp.getStreamPlayer().pause();
    }

    /**
     * Stop the player.
     */
    public void stop() {
        mainApp.getStreamPlayer().stop();
    }


    /**
     * Create a soundboard button
     *
     * @param audioClip the audio clip linked to the button
     * @return a Button
     */
    public Button createButton(AudioClip audioClip, int buttonsize) {
        JFXButton button = new JFXButton();

        button.setOnAction(event -> {
            //Play the song
            if (!editMode.getValue()) {
                playSong(audioClip.getPath());
            }
        });
        //Edit container
        HBox edit = new HBox();
        edit.setAlignment(Pos.BOTTOM_CENTER);
        edit.setVisible(false);

        Button editBtn = new Button();
        FontIcon editGraphic = new FontIcon(Ionicons4Material.CREATE);
        editGraphic.setIconSize(24);
        editBtn.setGraphic(editGraphic);
        editBtn.setAlignment(Pos.CENTER_LEFT);
        editBtn.getStyleClass().add("controlButton");
        editBtn.setOnAction(event -> {
            showEditAudioDialog(audioClip);
        });

        Button del = new Button();
        FontIcon delGraphic = new FontIcon(Ionicons4IOS.REMOVE_CIRCLE);
        delGraphic.setIconSize(24);
        del.setGraphic(delGraphic);
        del.setAlignment(Pos.CENTER_RIGHT);
        del.getStyleClass().add("controlButton");
        del.setOnAction(event -> {
            deleteAudioClip(audioClip);
        });


        edit.getChildren().addAll(editBtn, del);
        editContainers.add(edit);

        VBox container = new VBox();
        container.setAlignment(Pos.BOTTOM_CENTER);
        edit.setAlignment(Pos.BOTTOM_CENTER);
        container.setFillWidth(true);
        Label name = new Label(audioClip.getName());
        name.setAlignment(Pos.CENTER);
        edit.setPadding(new Insets(buttonsize / 4, 0, 0, 0));
        container.getChildren().addAll(name, edit);
        button.setGraphic(container);
        if (isColorLight(audioClip.getColor())) {
            delGraphic.setIconColor(Color.WHITE);
            editGraphic.setIconColor(Color.WHITE);
            name.setTextFill(Color.WHITE);
        } else {
            delGraphic.setIconColor(Color.BLACK);
            editGraphic.setIconColor(Color.BLACK);
            name.setTextFill(Color.BLACK);
        }
        button.getStyleClass().add("jfx-button");
        button.setMaxSize(buttonsize, buttonsize);
        button.setPrefWidth(buttonsize);
        button.setRipplerFill(audioClip.getColor().darker());
        button.setBackground(new Background(new BackgroundFill(audioClip.getColor(), new CornerRadii(3), new Insets(0, 0, 0, 0))));
        button.setContentDisplay(ContentDisplay.BOTTOM);
        button.setAlignment(Pos.CENTER);
        button.setPrefHeight(buttonsize);
        button.setMinHeight(buttonsize);
        button.setMinWidth(buttonsize);
        button.setCache(true);


        return button;
    }

    /**
     * Return if the color is dark or light
     *
     * @param color the color
     * @return true if the color is light and false is the color is dark
     */
    public boolean isColorLight(Color color) {
        double darkness = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }

    /**
     * Handle the button event to enable or disable edit mode
     *
     * @param actionEvent
     */
    public void editBtn(ActionEvent actionEvent) {
        if (editMode.getValue()) {
            disableEditMode();
        } else {
            enableEditMode();
        }
    }


    /**
     * Enable edit mode
     */
    public void enableEditMode() {
        for (HBox edit_container : editContainers) {
            edit_container.setVisible(true);
        }
        setEditMode(true);

    }

    /**
     * Disable edit mode
     */
    public void disableEditMode() {
        for (HBox edit_container : editContainers) {
            edit_container.setVisible(false);
        }
        setEditMode(false);

    }

    public boolean isEditMode() {
        return editMode.get();
    }

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode.set(editMode);


    }

    /**
     * Set the MainApp
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Get the MainApp
     *
     * @return MainApp
     */
    public MainApp getMainApp() {
        return mainApp;
    }

    /**
     * Handle click on the hamburger
     *
     * @param mouseEvent
     */
    public void handleHamburger(MouseEvent mouseEvent) {
        if (drawer.isClosed() || drawer.isClosing()) {
            drawer.open();
        } else {
            drawer.close();
        }
    }

    /**
     * Load the soundboard and generate buttons
     *
     * @param index index of the soundboard
     */
    public void loadSoundboard(int index) {
        grid.getChildren().clear();
        board.clear();
        if (mainApp.getSoundBoards().size() > 0) {

            for (AudioClip audioClip : mainApp.getSoundBoards().get(index).getAudioClips()) {

                board.add(createButton(audioClip, 120));
            }

            grid.getChildren().addAll(board);
            if (editMode.getValue()) {
                enableEditMode();
            }
            try {
                soundboardLabel.setText(mainApp.getSoundBoards().get(index).getName());
            } catch (ArrayIndexOutOfBoundsException e) {
                soundboardLabel.setText("");
            }
        }
    }

    /**
     * Handle the add button event
     * Add a button to the soundboard grid
     *
     * @param actionEvent
     */
    public void addBtn(ActionEvent actionEvent) {
        showAddAudioDialog();

    }

    /**
     * Show add audio dialog
     */
    public void showAddAudioDialog() {

        jfxDialog = new AddAudioClipDialog(this);
        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
            jfxDialog = null;
        });
    }


    /**
     * Show add audio dialog
     *
     * @param audioClip the audioclip if editted
     */
    public void showEditAudioDialog(AudioClip audioClip) {


        jfxDialog = new EditAudioClipDialog(this, audioClip);
        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
            jfxDialog = null;
        });
    }

    public File openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        String home = System.getProperty("user.home");

        fileChooser.setTitle("Choose audio file");
        fileChooser.setInitialDirectory(new File(home));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", SupportedAudio.listSupportedAudio("*.")));

        File audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (audiofile != null) {
            return audiofile;
        }
        return null;
    }

    /**
     * Handle the add soundboard button
     *
     * @param actionEvent
     */
    public void handleAddSoundboard(ActionEvent actionEvent) {
        jfxDialog = new AddSoundboardDialog(this);
        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
            jfxDialog = null;
        });
    }


    public void editSoundboardDialog(SoundBoard soundBoard) {
        jfxDialog = new EditSoundboardDialog(this, soundBoard);
        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
            jfxDialog = null;
        });
    }

    /**
     * Add a sounboard to database and UI
     *
     * @param soundboardName the name of the soundboard
     */
    public void addSoundBoard(String soundboardName) {
        if (!soundboardName.isEmpty()) {
            jfxDialog.close();
            mainApp.getSoundBoards().add(DBHelper.addSounboard(soundboardName));
            listview.getSelectionModel().selectLast();
            if (mainApp.getSoundBoards().size() == 1) {
                addAudioClipBtn.setDisable(false);
            }
        }
    }

    /**
     * Delete a soundboard in database and UI
     *
     * @param soundBoard
     */
    public void deleteSoundboard(SoundBoard soundBoard) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(dialogstackpane, "Do you really want to delete this soundboard ?");
        confirmationDialog.getOkButton().setOnAction(event -> {
            mainApp.getSoundBoards().remove(findSoundboardIndex(soundBoard));
            listview.getItems().remove(soundBoard);
            grid.getChildren().clear();
            DBHelper.deleteSoundboard(soundBoard);
            if (mainApp.getSoundBoards().size() == 0) {
                addAudioClipBtn.setDisable(true);
            }
            confirmationDialog.close();
            dialogstackpane.toBack();
        });
        confirmationDialog.show();
        dialogstackpane.toFront();
        confirmationDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
        });


    }


    public void editSoundboard(SoundBoard soundBoard) {
        DBHelper.editSoundBoard(soundBoard);
        mainApp.getSoundBoards().get(findSoundboardIndex(soundBoard)).setName(soundBoard.getName());
        loadSoundboard(currentSoundboardIndex);
        jfxDialog.close();
    }


    /**
     * Add audio clip to current soundboard and UI
     *
     * @param audioClip
     */
    public void addAudioClip(AudioClip audioClip) {
        if (!audioClip.getName().isEmpty() && audioClip.getFile().isFile()) {

            mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().add(DBHelper.addAudioClip(currentSoundboardIndex + 1, audioClip));
            loadSoundboard(currentSoundboardIndex);
            jfxDialog.close();
        }
    }

    /**
     * Edit audio clip in database and UI
     *
     * @param audioClip
     */
    public void editAudioClip(AudioClip audioClip) {
        if (!audioClip.getName().isEmpty() && audioClip.getFile().isFile()) {
            DBHelper.editAudioClip(audioClip);
            mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().set(findAudioClipIndex(audioClip), audioClip);
            loadSoundboard(currentSoundboardIndex);
            jfxDialog.close();
        }
    }

    /**
     * Delete audio clip from database and UI
     *
     * @param audioClip
     */
    private void deleteAudioClip(AudioClip audioClip) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(dialogstackpane, "Do you really want to delete this audio clip ?");
        confirmationDialog.getOkButton().setOnAction(event -> {
            DBHelper.removeAudioClip(audioClip);
            int index = findAudioClipIndex(audioClip);
            mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().remove(index);
            grid.getChildren().remove(index);
            board.remove(index);
        });
        confirmationDialog.show();
        dialogstackpane.toFront();

    }

    /**
     * Return the index of the audio clip in the list
     *
     * @return index
     */
    private int findAudioClipIndex(AudioClip audioClip) {
        for (int i = 0; i < mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().size(); i++) {
            if (audioClip.sameID(mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int findSoundboardIndex(SoundBoard soundBoard) {
        for (int i = 0; i < mainApp.getSoundBoards().size(); i++) {
            if (soundBoard.equals(mainApp.getSoundBoards().get(i))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * bind volume
     */
    public void volumeControl() {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainApp.getStreamPlayer().setGain((double) newValue / 100.00);
        });
    }


    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {

    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {

    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {

    }

    public void handleStop(ActionEvent actionEvent) {
        mainApp.getStreamPlayer().stop();
    }

    public StackPane getDialogstackpane() {
        return dialogstackpane;
    }

}
