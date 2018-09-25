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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import mojito_soundboard.MainApp;
import mojito_soundboard.model.*;
import mojito_soundboard.util.DBHelper;
import org.kordamp.ikonli.ionicons4.Ionicons4IOS;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainController {

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

    /**
     * Setting container
     */
    @FXML
    public Parent settings;

    /**
     * Information dialog about the file
     */
    private InfoDialog infoDialog;

    @FXML
    ScrollPane scrollpane;

    private JFXDialog addSoundboardDialog;
    private JFXDialog addAudioDialog;

    /**
     * Save current index of the selected soundboard
     */
    private int currentSoundboardIndex;

    /**
     * Called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        editMode = new SimpleBooleanProperty(false);
        board = FXCollections.observableArrayList();
        editContainers = new ArrayList<>();
        infoDialog = new InfoDialog();

        JFXScrollPane.smoothScrolling(scrollpane);

        //Hamburger initialization
        HamburgerBasicCloseTransition burgerTask = new HamburgerBasicCloseTransition(hamburger);
        burgerTask.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();
        });


        Platform.runLater(() -> {
            listview.setItems(mainApp.getSoundBoards());
            listview.setCellFactory(param -> new SoundBoardCell());
            listview.getSelectionModel().selectedIndexProperty().addListener(event -> {
                loadSoundboard(listview.getSelectionModel().getSelectedIndex());
                currentSoundboardIndex = listview.getSelectionModel().getSelectedIndex();
            });


            loadSoundboard(0);


        });

        loadSettings();

    }


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
     * Play the audio clip
     *
     * @param path
     */
    public void play(String path) {

        //TODO Play
    }

    /**
     * Stop playing the audio clip
     */
    private void stop() {

        //TODO stop
    }


    /**
     * Create a soundboard button
     *
     * @param audioClip the audio clip linked to the button
     * @return a Button
     */
    public Button createButton(AudioClip audioClip, int buttonsize) {
        Button button = new Button();

        button.setOnAction(event -> {
            // Call play()
        });

        //Edit container
        HBox edit = new HBox();
        edit.setAlignment(Pos.BOTTOM_LEFT);
        edit.setVisible(false);
        Button file = new Button();

        FontIcon filegraphic = new FontIcon(Ionicons4IOS.DOCUMENT);
        filegraphic.setIconSize(24);
        file.setGraphic(filegraphic);
        file.getStyleClass().add("controlButton");
        file.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            if (audiofile != null) {
                audioClip.setFile(audiofile);
            }

        });

        Button repeat = new Button();
        FontIcon repeatgraphic = new FontIcon(Ionicons4IOS.REPEAT);
        repeatgraphic.setIconSize(24);
        repeat.setGraphic(repeatgraphic);
        repeat.getStyleClass().add("controlButton");

        Button info = new Button();
        FontIcon infographic = new FontIcon(Ionicons4IOS.INFORMATION_CIRCLE);
        infographic.setIconSize(24);
        info.setGraphic(infographic);
        info.getStyleClass().add("controlButton");
        info.setOnAction(event -> {
            infoDialog = new InfoDialog(audioClip.getName(), audioClip.getPath(), audioClip.getShortcut());
            infoDialog.showAndWait();

        });


        edit.getChildren().addAll(file, repeat, info);
        editContainers.add(edit);
        button.setGraphic(edit);

        button.setMaxSize(buttonsize, buttonsize);
        button.setPrefWidth(buttonsize);
        button.setContentDisplay(ContentDisplay.BOTTOM);
        button.setAlignment(Pos.BOTTOM_LEFT);
        button.setPrefHeight(buttonsize);
        button.setMinHeight(buttonsize);
        button.setMinWidth(buttonsize);
        button.setCache(true);
        button.setText(audioClip.getName());


        return button;
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
            setEditMode(true);
        }
    }

    /**
     * Disable edit mode
     */
    public void disableEditMode() {
        for (HBox edit_container : editContainers) {
            edit_container.setVisible(false);
            setEditMode(false);
        }
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
        // Load first soundboard
        grid.getChildren().clear();
        board.clear();
        for (AudioClip audioClip : mainApp.getSoundBoards().get(index).getAudioClips()) {

            board.add(createButton(audioClip, 120));
        }

        grid.getChildren().addAll(board);
        if (editMode.getValue()) {
            enableEditMode();
        }
    }

    /**
     * Handle the add button event
     * Add a button to the soundboard grid
     *
     * @param actionEvent
     */
    public void addBtn(ActionEvent actionEvent) {

        JFXDialogLayout content = new JFXDialogLayout();
        GridPane grid = new GridPane();


        JFXTextField name = new JFXTextField();
        JFXTextField file = new JFXTextField();
        JFXTextField shortcut = new JFXTextField();
        name.setPromptText("Name");
        file.setPromptText("File");
        file.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File audiofile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            if (audiofile != null) {
                file.setText(audiofile.getPath());
            }


        });
        shortcut.setPromptText("Shortcut");
        grid.add(new Label("Audio clip name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Soundboard name:"), 0, 1);
        grid.add(file, 1, 1);
        grid.add(new Label("Shortcut:"), 0, 2);
        grid.add(shortcut, 1, 2);
        grid.setHgap(10);
        content.setBody(grid);
        JFXButton ok = new JFXButton("Ok");
        ok.setOnAction(event -> {
            addAudioClip(name.getText(), new File(file.getText()), shortcut.getText());
        });
        content.setActions(ok);

        addAudioDialog = new JFXDialog(dialogstackpane, content, JFXDialog.DialogTransition.CENTER);


        addAudioDialog.show();
        dialogstackpane.toFront();
        addAudioDialog.setOnDialogClosed(event -> {
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
        ok.setOnAction(event -> {
            addSoundBoard(name.getText());
        });
        content.setActions(ok);

        addSoundboardDialog = new JFXDialog(dialogstackpane, content, JFXDialog.DialogTransition.CENTER);


        addSoundboardDialog.show();
        dialogstackpane.toFront();
        addSoundboardDialog.setOnDialogClosed(event -> {
            dialogstackpane.toBack();
        });
    }

    private void addSoundBoard(String soundboard_name) {
        if (!soundboard_name.isEmpty()) {
            addSoundboardDialog.close();
            mainApp.getSoundBoards().add(new SoundBoard(soundboard_name));
            listview.getSelectionModel().selectLast();
            DBHelper.addSounboard(soundboard_name);
        }
    }

    private void addAudioClip(String name, File file, String shortcut) {
        if (!name.isEmpty() && file.isFile()) {
            AudioClip audioClip;
            if (shortcut.length() > 0) {
                audioClip = new AudioClip(name, file, shortcut);

            } else {
                audioClip = new AudioClip(name, file);

            }
            mainApp.getSoundBoards().get(currentSoundboardIndex).getAudioClips().add(audioClip);
            loadSoundboard(currentSoundboardIndex);
            DBHelper.addAudioClip(currentSoundboardIndex, name, file, shortcut);
            addAudioDialog.close();
        }
    }
}
