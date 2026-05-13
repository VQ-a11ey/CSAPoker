package com.poker;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label; // for the commentary like welcome to poker or smth like that
import javafx.scene.text.Font; // different fonts
import javafx.scene.paint.Color;
import javafx.scene.control.Slider; // maybe we can use this for the raise amount?

public class PokerApp extends Application {
    private HBox middleCards;
    private HBox playerHand;
    private Label commentary;

    public void start(Stage primaryStage) {
        // get rid of scanner and replace w/ the buttons here probably? 
        // idk how to connect their acctions with the buttons to the code we have rn
        // need to figure out how to implement the multiplayer thing on GUI
        // maybe flip their hand over after their decision and flip it when it's their turn
        // useful website: https://docs.oracle.com/javafx/2/ui_controls/jfxpub-ui_controls.htm
        
        BorderPane background = new BorderPane();
        background.setStyle("-fx-background-color: #079740");//???idk how)
        commentary = new Label("Play poker!");
        commentary.setFont(new Font("Times New Roman", 12));
        middleCards = new HBox(10);
        middleCards.setAlignment(Pos.CENTER);
        //missing lots of stuff
        Button fold = new Button("Fold");
        Button call = new Button("Check/Call");
        Button raise = new Button("Raise");
        fold.setStyle("-fx-base: rgb(224, 211, 211)");
        call.setStyle("-fx-base: rgb(224, 211, 211)");
        raise.setStyle("-fx-base: rgb(224, 211, 211)");
        //fold.setOnAction();//???
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}