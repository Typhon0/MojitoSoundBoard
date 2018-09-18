package mojito_soundboard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class Controller {

    @FXML
    FlowPane grid;

    ArrayList<Button> board;

    @FXML
    public void initialize() {
        board = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Button button = new Button();
            button.setMaxSize(100, 100);
            button.setPrefWidth(100);
            button.setPrefHeight(100);
            button.setMinHeight(100);
            button.setMaxHeight(100);
            board.add(button);
        }

        grid.getChildren().addAll(board);


    }
}
