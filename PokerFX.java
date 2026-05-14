package com.example;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


//wow
//ts makes me wanna kms
//:3


public class PokerFX extends Application {
    private Game game;
    @Override
    public void start(Stage stage) {
        Label title = new Label("mini Poker Game, APCSA version");
        Label prompt = new Label("How many players?");
        TextField num = new TextField();
        num.setPromptText("Enter number of ppl");
        num.setPrefWidth(60);
        num.setMaxWidth(60);
        Button startb = new Button("Start");
        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");
        root.getChildren().addAll(title, prompt, num, startb);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Poker Game");
        stage.show();

        startb.setOnAction(e -> {
            int players;
            try {
                players = Integer.parseInt(num.getText().trim());
            } catch (Exception ex) {
                return;
            }
            game = new Game(players);
            root.getChildren().clear();
            Label givNames = new Label("Enter player names (again sorry lol idfk how to fix ts):");
            ArrayList<TextField> names = new ArrayList<>();
            for (int i = 0; i < players; i++) {
                TextField tf = new TextField();
                tf.setPromptText("Player " + (i + 1));
                tf.setMaxWidth(120);
                names.add(tf);
            }
            Button confirm = new Button("Confirm");
            root.getChildren().add(givNames);
            root.getChildren().addAll(names);
            root.getChildren().add(confirm);
            confirm.setOnAction(ev -> {
                for (int i = 0; i < players; i++) {
                    String name = names.get(i).getText().trim();
                    if (name.isEmpty()) {
                        name = "Player " + (i + 1);
                    }
                    game.getPlayers().get(i).setName(name);
                }
                root.getChildren().clear();
                Label status = new Label("click to run game. check console. its still on fricking console cuz wer're going to have to change so much stufff");
                Button runGame = new Button("Run Game");
                root.getChildren().addAll(status, runGame);
                runGame.setOnAction(ev2 -> {
                    new Thread(() -> game.runEntireGame()).start();
                });
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
