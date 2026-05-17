package com.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PokerFX extends Application {

    private Game game;
    private Label commentary;
    private Label playerStats;
    private Label currentPlayer; 
    private Button callorcheckButton, foldButton, raiseButton; 
    private Label playerHand;
    private Label cards; 
    private MediaPlayer mediaPlayer;
    private TextField raiseField;
    private HBox raiseBox;
    private Label winnerLabel;
    private Label dealerLabel;

    @Override
    public void start(Stage stage) {
        // for background, i'm not sure if this works or not
        StackPane rootPane = new StackPane();
        URL bgUrl = getClass().getResource("/pokerbackground.png");
        if (bgUrl != null) {
            rootPane.setStyle("-fx-background-image: url('" + bgUrl.toExternalForm() + "');" + "-fx-background-size: cover;" + "-fx-background-position: center;");
        } else {
            rootPane.setStyle("-fx-background-image: #1a3a1a;");
        }
        // for music
        URL musicUrl = getClass().getResource("/FIREpokermusic.m4a");
        if (musicUrl != null) {
            Media media = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
        // start game
        Label intro = new Label("Welcome to Poker!");
        Label prompt = new Label("How many players? ");
        TextField numberOfPlayers = new TextField();
        numberOfPlayers.setPromptText("Enter number of players: ");
        numberOfPlayers.setMaxWidth(100);
        Button startGame = new Button("Start");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 14px;");
        root.getChildren().addAll(intro, prompt, numberOfPlayers, startGame);
        rootPane.getChildren().add(root);

        Scene scene = new Scene(rootPane, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Poker Game");
        stage.show();
        // set on action assigns actions to buttons
        startGame.setOnAction(e -> {
            int numOfPlayers;
            try {
                numOfPlayers = Integer.parseInt(numberOfPlayers.getText().trim());
                if (numOfPlayers < 2) {
                    throw new Exception();
                }
            } catch (Exception ex) {
                prompt.setText("Not enough players! Oh no!");
                return;
            }
            // roo is for player name input
            VBox roo = new VBox(10);
            roo.setAlignment(Pos.CENTER);
            roo.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 14px;");

            Label playerNames = new Label("Enter player names: ");
            roo.getChildren().add(playerNames);
            // for creating an arraylist of names to put into games later on
            ArrayList<TextField> names = new ArrayList<>();

            for (int i = 0; i < numOfPlayers; i++) {
                TextField playerField = new TextField();
                playerField.setPromptText("Player " + (i + 1));
                playerField.setMaxWidth(120);
                names.add(playerField);
                roo.getChildren().add(playerField);
            }

            Button confirmButton = new Button("Confirm");
            roo.getChildren().add(confirmButton);

            Scene nextScene = new Scene(roo, 400, 400);
            stage.setScene(nextScene);

            confirmButton.setOnAction(ev -> {
                ArrayList<String> playerNames2 = new ArrayList<>();

                for (TextField field : names) {
                    String name = field.getText();
                    if (name.isEmpty()) {
                        int playerNumber = names.indexOf(field) + 1;
                        playerNames2.add("Player " + playerNumber);
                    } else {
                        playerNames2.add(name);
                    }

                }

                game = new Game(playerNames2); 
                game.resetRound(); 
                pokerTable(stage);

            });
        });
    }

    //below this is the gameboard , above this is getting the names and players
    private void pokerTable(Stage stage) {
        VBox ro = new VBox(20);
        ro.setAlignment(Pos.CENTER);
        ro.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 20px");

        commentary = new Label("Starting Game!");
        commentary.setStyle("-fx-text-fill: white;");
        

        
        currentPlayer = new Label(); 
        currentPlayer.setStyle("-fx-text-fill: white;");

        playerStats = new Label("Stats over here");
        playerHand = new Label("Your Hand");
        cards = new Label("Community Cards");

        updateCurrentPlayerInfo();

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        callorcheckButton = new Button("Check");
        foldButton = new Button("Fold");
        raiseButton = new Button("Raise");
        raiseField = new TextField();
        raiseField.setPromptText("raise amount");
        raiseField.setMaxWidth(120);

        Button confirmRaiase = new Button("confirm");
        Button cancelRaise = new Button("cancel");

        raiseBox = new HBox(10, raiseField, confirmRaiase, cancelRaise);
        raiseBox.setAlignment(Pos.CENTER);
        raiseBox.setVisible(false);
        raiseBox.setManaged(false);

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
                int amount = Integer.parseInt(raiseField.getText()); 

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


        controls.getChildren().addAll(callorcheckButton, foldButton, raiseButton);
        ro.getChildren().addAll(commentary, playerStats, playerHand, cards, controls);
        ro.getChildren().add(raiseBox);
        stage.getScene().setRoot(ro);
        stage.setWidth(600);
        stage.setHeight(400);
    }

   
    private void updateCallCheckButton() {
        Player p = game.getCurrentPlayer();
        if (p.getBet() == game.getCurrent()) {
            callorcheckButton.setText("Check");
        } else {
            callorcheckButton.setText("Call");
        }
    }

    private void updateCurrentPlayerInfo(){
        Player current = game.getCurrentPlayer();
        currentPlayer.setText("Current Player: " + current.getName());
        playerStats.setText(current.getName()+" | Chips: "+current.getChips()+ "| Current Bet: "+game.getCurrent()+"| Pot: "+game.getPot());
        
        //need to make private...:( im lazy so fix later!!
        playerHand.setText("Your Hand (we have to make this private later right): " + current.getCardOne().getName()+"| "+current.getCardTwo().getName());
        StringJoiner middle = new StringJoiner("| "); //? need to fix cuz we didn't learn abt stringjoiners yet
        for(Card c:game.getMiddleCards()){
            middle.add(c.getName());
        }
        if(game.getMiddleCards().isEmpty())
            cards.setText("No community cards so far");
        else
            cards.setText("Community Cards: " + middle.toString());
    
    }

    //
    // later just stuff all update() methods into here and just call this every time instead of all the stuff individually
    //
    private boolean winnershown = false;
    private void refreshUI(){
        updateCurrentPlayerInfo();
        updateCallCheckButton();
        updateActionState();
        if (game.getRoundCount() == 4 && !winnershown) {
            winnershown = true;
            showWinnerScreen();
        }
    }


    
    // this is easier than 52 else if statements, just title the images as rank of suit.png
    public Image getCards(Card card) {
        Image placeholder = new Image("/redman.png"); // placeholder
        // String fileName = card.getName();
        return placeholder; // later change this to return new Image("/" + fileName + ".png")
    }

    public void loan() {
  Image image = new Image("/loanShark.png");
        commentary = new Label("Borrow some money from Shark?");
    Button confirmButton = new Button("yes");
       Button denyButton = new Button("i'd rather not :C ");
    }

    public boolean canAct(){
        if (game.getPlayers().size()<=1) return false;
        if(game.getRoundCount()>=4)return false;
        return true;
    }
    private void updateActionState() {
        if (game.getPlayers().size() <= 1) {
            callorcheckButton.setDisable(true);
            foldButton.setDisable(true);
            raiseButton.setDisable(true);
            return;
        }
        callorcheckButton.setDisable(false);
        foldButton.setDisable(false);
        raiseButton.setDisable(false);
    }

    private void showWinnerScreen() {
        VBox endScreen = new VBox(15);
        endScreen.setAlignment(Pos.CENTER);
        winnerLabel = new Label();
        Player winner = game.findWinner().get(0);
        String hand = game.handType(winner);
        winnerLabel.setText(
            "winner: " + winner.getName() +
            "\nHand: " + hand +
            "\nchips won: " + game.getPot()
        );
        Button restart = new Button("play again");
        restart.setOnAction(e -> {
            game.resetRound();
            pokerTable((Stage) restart.getScene().getWindow());
        });
        endScreen.getChildren().addAll(winnerLabel, restart);
        Stage stage = (Stage) callorcheckButton.getScene().getWindow();
        stage.getScene().setRoot(endScreen);
        }  

    public static void main(String[] args) {
        launch(args);
    }
}
