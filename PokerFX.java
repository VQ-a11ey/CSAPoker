package com.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PokerFX extends Application {
    //forGame
    private Game game;
    private MediaPlayer mediaPlayer;
    private boolean winner = false;
    //UI
    private Label commentary; //next to dealer
    private Label pot;
    private Label communityCards; 
    private Label currentBet;
    private VBox playerSeat;
    private Label currentPlayer;
    private Button callorcheckButton, foldButton, raiseButton; //nextRoundButton;
    private TextField raiseAmountTextField;
    private HBox raiseBox;
// below is the game setup
    @Override
    public void start(Stage stage) {
        //background
        StackPane rootPane = new StackPane(); // rootpane according to google "a lightweight container that acts as the hidden backbone" - basically the background!
        URL bgUrl = getClass().getResource("/pokerbackground.png"); // background picture from resources
        if (bgUrl != null) {
            rootPane.setStyle("-fx-background-image: url('" + bgUrl.toExternalForm() + "');" + "-fx-background-size: cover;" + "-fx-background-position: center;"); // background image, how much of the window it covers, and where
        } else { // if not given an image make the background dark green
            rootPane.setStyle("-fx-background-image: #2b652b;"); // -fx-whatever is used for setting background, positions, fonts, etc.
        }
        // music
        URL musicUrl = getClass().getResource("/FIREpokermusic.m4a");
        if (musicUrl != null) {
            Media media = new Media(musicUrl.toExternalForm()); // to external converts url to something javafx can interpret and put into the window
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
        startGame(stage,rootPane);
    }
// I have split the music and background from the start game scene
    private void startGame(Stage stage, StackPane rootPane){
        VBox box = new VBox(16); // vbox is vertical box, it is used for buttons, labels, etc
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(340);
        box.setStyle( "-fx-background-color: #8c714db8; -fx-background-radius:18; -fx-padding: 36 40 36 40;"); 
        // setStyle allows u to change background color, text, ... u get the idea, padding makes sure the text inside the box does not touch the border btw
        // text below
        Label intro = new Label("Welcome to Poker!");
        intro.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f0c040; -fx-font-family: 'Times New Roman';");
        Label prompt = new Label("How many players? ");
        prompt.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 15px;");

        TextField numOfPlayersField = new TextField(); //text field allows users to enter stuff, works like scanner
        numOfPlayersField.setPromptText("Enter number of players: ");
        numOfPlayersField.setMaxWidth(100);
        numOfPlayersField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #f0c040; -fx-border-radius: 6; -fx-background-radius: 6;");


        Label errorLabel = new Label(""); // in case there are less than 2 players
        errorLabel.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 12px;");
        Button startButton = new Button("Start"); // buttons can be pressed and set on actions
        //set on action: when you press the button an action occurs WOW!
        startButton.setOnAction(e -> { //pay attention to formatting, it's a bit wierd
            try{
                int n = Integer.parseInt(numOfPlayersField.getText().trim());
                if (n < 2){
                    throw new Exception(); // not enough players
                }
                showNameScreen(stage, rootPane, n);
            } catch (Exception ex){
                errorLabel.setText("Not enough players!!, Please enter a valid number: ");
            }
        });
        // someone please check what this does, i'm not excatly sure 
        box.getChildren().addAll(intro, prompt, numOfPlayersField, errorLabel, startButton);
        rootPane.getChildren().setAll(box);

        /*
        if (stage.getScene() == null){
            stage.getScene(new Scene(rootPane, 900, 650));
            stage.setTitle("Welcome to Poker!");
            stage.show();
        } else {
            stage.getScene().setRoot(rootPane);
        }  
        */
    }
// for name entry
    private void showNameScreen(Stage stage, StackPane rootPane, int n){
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(360);
        box.setStyle("-fx-background-color: #8c714db8; -fx-background-radius: 18; -fx-padding: 32 44 32 44;");

        Label playerNames = new Label("Enter player names: ");
        playerNames.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f0c040; -fx-font-family: 'Times New Roman';");

        ArrayList<TextField> namesFields = new ArrayList<>(); // get all names of players
        for (int i = 0; i < n; i++) {
            TextField playerField = new TextField();
            playerField.setPromptText("Player " + (i + 1));
            playerField.setMaxWidth(220);
            playerField.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 14px; -fx-padding: 5 8;");
            namesFields.add(playerField);
            box.getChildren().add(playerField);
        }

        Button confirmButton = button("Deal cards!"); // confirm to deal cards
        confirmButton.setOnAction(ev -> {
            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < namesFields.size(); i++) {
                String name = namesFields.get(i).getText().trim();
                if (name.isEmpty()) {
                    int playerNumber = i + 1;
                    names.add("Player " + playerNumber);
                } else {
                    names.add(name);
                }
            }
            game = new Game(names);
            game.resetRound();
            winner = false;
            showPokerTble(stage, rootPane);
        });
     //???????   box.getChildren().addAll(0, java.util.List.of(intro)); 
        box.getChildren().add(confirmButton);
        rootPane.getChildren().setAll(box);
    }

//below this is the poker gameboard
    private void pokerTable(Stage stage, StackPane rootPane) {
        //bk outer layout
        BorderPane table = new BorderPane();
        table.setStyle("-fx-background-color:transparent;");

        HBox topCommentary = dealer();
        table.setTop(topCommentary);
        BorderPane.setAlignment(topCommentary, Pos.CENTER); // place commentary of dealer at center
        
        // table community cards
        StackPane centr = centr();
        table.setCenter(centr);
        
        //bottom controls
        VBox bottom = bottum();
        table.setBottom(bottom);
        BorderPane.setAlignment(bottom, Pos.CENTER);

        // other player seats
        playerSeat = new VBox(10);
        playerSeat.setAlignment(Pos.CENTER);
        playerSeat.setPadding(new Insets(10));
        table.setLeft(playerSeat);

        rootPane.getChildren().setAll(table);
        stage.getScene().setRoot(rootPane);
        stage.setWidth(1000);
        stage.setHeight(680);

        refreshUI();

    }

// dealer commentary method
    private HBox dealer(){
        HBox deal = new HBox(14);
        deal.setAlignment(Pos.CENTER);
        deal.setPadding(new Insets(12, 20, 6, 20));
        deal.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");

        //VERY COOL SHARK DEALER WOWOWOWWOWOWOWO
        ImageView dealerImage = new ImageView();
        URL dealerUrl = getClass().getResource("/dealer.png");
        if (dealerUrl != null){
            dealerImage.setImage(new Image(dealerUrl.toExternalForm()));
        }
        dealerImage.setFitHeight(80);
        dealerImage.setPreserveRatio(true);

        //commentary speech from dealer shark
        StackPane speech = new StackPane();
        Rectangle bubble = new Rectangle(320, 60);
        bubble.setArcWidth(18);
        bubble.setArcHeight(18);
        bubble.setFill(Color.ALICEBLUE);
        bubble.setStroke(Color.BLACK);
        bubble.setStrokeWidth(2);

        commentary = new Label("Welcome to gamble ur manee hehehe!");
        commentary.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman';");

        speech.getChildren().addAll(bubble, commentary);
        deal.getChildren().addAll(dealerImage, speech);
        return deal;
    }

    private StackPane centr(){
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(10));
        
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(460);
     
        pot = new Label("POT: 0");
        pot.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f0c040; -fx-font-family: 'Times New Roman';");

        currentBet = new Label("Current Bet: 0");
        currentBet.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #dddddd; -fx-font-family: 'Times New Roman';");

        communityCards = new Label("No cards are passed out yet");
        communityCards.setStyle("-fx-font-size: 13px; -fx-text-fill: white; -fx-wrap-text: true; -fx-text-alignment: center;");
        communityCards.setMaxWidth(440);
        communityCards.setWrapTet(true);

        box.getChildren().addAll(pot, currentBet, communityCards);
        stackPane.getChildren().addAll(box);
        return stackPane;
    }
// player action
    private VBox bottum(){
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8, 20, 14, 20));
        box.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        currentPlayer = new Label(".");
        currentPlayer.setStyle("-fx-text-fill: #f0c040; -fx-font-size: 15px; -fx-font-weight: bold;");
    // buttons for command
        callorcheckButton = new Button("Check");
        foldButton = new Button("Fold");
        raiseButton = new Button("Raise");
     // input for raise   
        raiseField = new TextField();
        raiseField.setPromptText("raise amount");
        raiseField.setMaxWidth(120);

        Button confirmRaiase = new Button("confirm");
        Button cancelRaise = new Button("cancel");

        raiseBox = new HBox(10, raiseField, confirmRaiase, cancelRaise);
        raiseBox.setAlignment(Pos.CENTER);
        raiseBox.setVisible(false);
        raiseBox.setManaged(false);
        
        HBox buttns = new HBox(14, callorcheckButton, foldButton, raiseButton);
        buttns.setAlignment(Pos.CENTER);
 
        
        foldButton.setOnAction(e -> {
            if(!canAct()) return;
            game.foldButton();
            commentary.setText("Folded");
            refreshUI();
        });

        callorcheckButton.setOnAction(e -> {
            if(!canAct()) return;
            game.callOrCheckButton();
            if (game.getCurrent() == 0) {
                commentary.setText("Checked");
            } else {
                commentary.setText("Called");
            }
            refreshUI();
        });

        raiseButton.setOnAction(e-> {
            raiseBox.setVisible(true);
            raiseBox.setManaged(true);
            callorcheckButton.setDisable(true);
            foldButton.setDisable(true);
        });
        confirmRaiase.setOnAction(e -> {
            if(!canAct()) return;
            try{
                int amount = Integer.parseInt(raiseField.getText()); //fixed the placeholder i think

                if(amount<=game.getCurrent()){
                    commentary.setText("must be higher");
                    return;
                }
                Player p = game.getCurrentPlayer();
                if(amount>p.getChips()){
                    commentary.setText("ur too broke");
                    return;
                }
                game.raiseButton(amount);  
                commentary.setText("raised  to " + amount);
                raiseBox.setVisible(false);
                raiseBox.setManaged(false);
                raiseField.clear();
                callorcheckButton.setDisable(false);
                foldButton.setDisable(false);
                refreshUI();
            } catch(Exception ex){
                commentary.setText("not valid");
            }
        });
        cancelRaise.setOnAction(e -> {
            raiseBox.setVisible(false);
            raiseBox.setManaged(false);
            raiseField.clear();
            callorcheckButton.setDisable(false);
            foldButton.setDisable(false);
        });
    // above done by viky

        box.getChildren().addAll(currentPlayer, buttns, raiseBox);
        return box;
    }
//update  methods, can be copied from vicky's code 
    // some are different tho idk.
 

    public static void main(String[] args) {
        launch(args);
    }
}
