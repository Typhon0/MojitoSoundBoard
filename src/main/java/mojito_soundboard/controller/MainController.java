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
import mojito_soundboard.model.*;
import mojito_soundboard.util.DBHelper;
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
     *
     */
    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label soundboardLabel;

    /**
     * Setting container
     */
    @FXML
    public Parent settings;

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

    private PlayService playService;
    ;

    private AudioClip currentAudioClip;

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
            showAddAudioDialog(audioClip.getName(), audioClip.getPath(), audioClip.getShortcut(), audioClip.getColor(), true);
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
            name.setTextFill(Color.WHITE);
        } else {
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
            System.out.println(currentSoundboardIndex);
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
        showAddAudioDialog("", "", "", null, false);

    }

    /**
     * Show add audio dialog
     *
     * @param audioClipName name of the audio clip
     * @param path          path of the audio file
     * @param shorcut       shortcut to play the audio file
     * @param edit          true if edit or false if add to database
     */
    public void showAddAudioDialog(String audioClipName, String path, String shorcut, Color color, boolean edit) {
        JFXDialogLayout content = new JFXDialogLayout();
        GridPane grid = new GridPane();


        JFXTextField name = new JFXTextField();
        JFXTextField file = new JFXTextField();
        JFXTextField shortcutField = new JFXTextField();
        JFXColorPicker jfxColorPicker = new JFXColorPicker();

        if (edit) {
            name.setText(audioClipName);

            file.setText(path);

            shortcutField.setText(shorcut);
            jfxColorPicker.setValue(color);

        }

        name.setPromptText("Name");
        file.setPromptText("File");
        file.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            if (audiofile != null) {
                file.setText(audiofile.getPath());
            }


        });
        shortcutField.setPromptText("Shortcut");
        grid.add(new Label("Audio clip name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Soundboard name:"), 0, 1);
        grid.add(file, 1, 1);
        grid.add(new Label("Shortcut:"), 0, 2);
        grid.add(shortcutField, 1, 2);
        Label colorL = new Label("Color:");

        GridPane.setMargin(colorL, new Insets(10, 0, 0, 0));
        grid.add(colorL, 0, 3);

        GridPane.setMargin(jfxColorPicker, new Insets(10, 0, 0, 0));
        grid.add(jfxColorPicker, 1, 3);
        grid.setHgap(10);
        content.setBody(grid);
        JFXButton ok = new JFXButton("Ok");
        ok.setStyle("-fx-background-color: GRAY");
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        ok.setOnAction(event -> {
            System.out.println(jfxColorPicker.getValue());
            AudioClip audioClip;
            if (shortcutField.getText().isEmpty()) {
                audioClip = new AudioClip(name.getText(), new File(file.getText()), jfxColorPicker.getValue());

            } else {
                audioClip = new AudioClip(name.getText(), new File(file.getText()), shortcutField.getText(), jfxColorPicker.getValue());
            }

            if (edit) {
                //Edit audio clip
                editAudioClip(audioClip);
            } else { // else add
                addAudioClip(audioClip);

            }
        });
        content.setActions(ok);

        jfxDialog = new JFXDialog(dialogstackpane, content, JFXDialog.DialogTransition.CENTER);


        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
        });
    }


    /**
     * Handle the add soundboard button
     *
     * @param actionEvent
     */
    public void handleAddSoundboard(ActionEvent actionEvent) {

        JFXDialogLayout content = new JFXDialogLayout();
        GridPane grid = new GridPane();


        JFXTextField name = new JFXTextField();
        name.setPromptText("Name");
        grid.add(new Label("Soundboard name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.setHgap(10);
        content.setBody(grid);
        JFXButton ok = new JFXButton("Ok");
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        ok.setStyle("-fx-background-color: GRAY");
        ok.setOnAction(event -> {
            addSoundBoard(name.getText());
        });
        content.setActions(ok);

        jfxDialog = new JFXDialog(dialogstackpane, content, JFXDialog.DialogTransition.CENTER);


        jfxDialog.show();
        dialogstackpane.toFront();
        jfxDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
        });
    }

    /**
     * Add a sounboard to database and UI
     *
     * @param soundboardName the name of the soundboard
     */
    private void addSoundBoard(String soundboardName) {
        if (!soundboardName.isEmpty()) {
            jfxDialog.close();
            mainApp.getSoundBoards().add(DBHelper.addSounboard(soundboardName));
            listview.getSelectionModel().selectLast();
        }
    }

    /**
     * Delete a soundboard in database and UI
     *
     * @param soundBoard
     */
    public void deleteSoundboard(SoundBoard soundBoard) {
        mainApp.getSoundBoards().remove(soundBoard.getId() - 1);
        listview.getItems().remove(soundBoard);
        grid.getChildren().clear();
        DBHelper.deleteSoundboard(soundBoard);

    }


    /**
     * Add audio clip to current soundboard and UI
     *
     * @param audioClip
     */
    private void addAudioClip(AudioClip audioClip) {
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
    private void editAudioClip(AudioClip audioClip) {
        if (!audioClip.getName().isEmpty() && audioClip.getFile().isFile()) {
            DBHelper.editAudioClip(audioClip);
            mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().set(audioClip.getId() - 1, audioClip);
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
        DBHelper.removeAudioClip(audioClip);
        System.out.println(mainApp.getSoundBoards().size());
        mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().remove(audioClip.getId() - 1);
        grid.getChildren().remove(audioClip.getId() - 1);
        board.remove(audioClip.getId() - 1);

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

    /**
     * Get the current audio clip
     *
     * @return
     */
    public AudioClip getCurrentAudioClip() {
        return currentAudioClip;
    }
}
