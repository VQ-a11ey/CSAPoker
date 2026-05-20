package com.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringJoiner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PokerFX extends Application {

    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 600;
    private static final double WINDOW_RATIO = WINDOW_WIDTH / WINDOW_HEIGHT;

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
    private StackPane rootPane;

    @Override
    public void start(Stage stage) {
        // for background
        rootPane = createBackgroundPane();
        // for music
        URL musicUrl = getClass().getResource("/FIREpokermusic.m4a");
        if (musicUrl != null) {
            Media media = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
        // start game
        Label intro = new Label("Welcome to Poker!");
        intro.setStyle("-fx-text-fill: white;");
        Label prompt = new Label("How many players? ");
        prompt.setStyle("-fx-text-fill: white;");
        TextField numberOfPlayers = new TextField();
        numberOfPlayers.setPromptText("2 - 6 players");
        numberOfPlayers.setMaxWidth(125);
        Button startGame = new Button("Start");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 18px;");
        root.getChildren().addAll(intro, prompt, numberOfPlayers, startGame);
        rootPane.getChildren().add(root);

        Scene scene = new Scene(rootPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Poker");
        Image icon = new Image("poker symbol.png");
        stage.getIcons().add(icon);
        lockWindowRatio(stage);
        stage.show();
        // set on action assigns actions to buttons
        startGame.setOnAction(e -> {
            int numOfPlayers;
            try {
                numOfPlayers = Integer.parseInt(numberOfPlayers.getText().trim());
                if (numOfPlayers < 2) {
                    throw new IllegalArgumentException();
                }
                if (numOfPlayers > 6) {
                    prompt.setText("Too many players! Max is 6.");
                    return;
                }
            } catch (Exception ex) {
                prompt.setText("Not enough players! Oh no!");
                return;
            }
            // roo is for player name input
            VBox roo = new VBox(10);
            roo.setAlignment(Pos.CENTER);
            roo.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 18px;");

            Label playerNames = new Label("Enter player names: ");
            playerNames.setStyle("-fx-text-fill: white;");
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

            rootPane.getChildren().clear();
            rootPane.getChildren().add(roo);

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
                pokerTable();

            });
        });
    }

    // below this is the gameboard , above this is getting the names and players
    private void pokerTable() {

        VBox ro = new VBox(20);
        ro.setAlignment(Pos.CENTER);
        ro.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 18px");

        commentary = new Label("Starting Game!");
        commentary.setStyle("-fx-text-fill: white;");

        currentPlayer = new Label();
        currentPlayer.setStyle("-fx-text-fill: white;");

        playerStats = new Label("Stats over here");
        playerHand = new Label("Your Hand");
        cards = new Label("Community Cards");
        playerStats.setStyle("-fx-text-fill: white;");
        playerHand.setStyle("-fx-text-fill: white;");
        cards.setStyle("-fx-text-fill: white;");

        updateCurrentPlayerInfo();

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        callorcheckButton = new Button("Call");
        foldButton = new Button("Fold");
        raiseButton = new Button("Raise");
        raiseField = new TextField();
        raiseField.setPromptText("raise amount");
        raiseField.setMaxWidth(120);

        Button confirmRaise = new Button("confirm");
        Button cancelRaise = new Button("cancel");

        raiseBox = new HBox(10, raiseField, confirmRaise, cancelRaise);
        raiseBox.setAlignment(Pos.CENTER);
        raiseBox.setVisible(false);
        raiseBox.setManaged(false);

        foldButton.setOnAction(e -> {
            if (!canAct())
                return;
            commentary.setText(game.getCurrentPlayer().getName() + " folded");
            game.fold();
            refreshUI();
        });

        callorcheckButton.setOnAction(e -> {
            if (!canAct())
                return;
            if (game.canCheck()) {
                commentary.setText(game.getCurrentPlayer().getName() + " checked");
                game.check();
            } else if (game.canCall() && game.getAmountToCall() != game.getCurrentPlayer().getChips()){
                commentary.setText(game.getCurrentPlayer().getName() + " called");
                game.call();
            }
            else{
                commentary.setText(game.getCurrentPlayer().getName() + " went all in!");
                game.call();
            }
            refreshUI();
        });

        raiseButton.setOnAction(e -> {
            raiseBox.setVisible(true);
            raiseBox.setManaged(true);
            callorcheckButton.setDisable(true);
            foldButton.setDisable(true);
        });
        confirmRaise.setOnAction(e -> {
            if (!canAct())
                return;
            try {
                int amount = Integer.parseInt(raiseField.getText());
                if (!game.canRaiseBy(amount + game.getAmountToCall())) {
                    commentary.setText("ur too broke");
                    return;
                }
                if (amount + game.getAmountToCall() >= game.getCurrentPlayer().getChips()){
                    commentary.setText(game.getCurrentPlayer().getName() + " went all in with a raise!");
                }
                else{
                    commentary.setText(game.getCurrentPlayer().getName() + " raised by " + amount);
                }
                game.raise(amount);
                raiseBox.setVisible(false);
                raiseBox.setManaged(false);
                raiseField.clear();
                callorcheckButton.setDisable(false);
                foldButton.setDisable(false);
                refreshUI();
            } catch (Exception ex) {
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
        rootPane.getChildren().clear();
        rootPane.getChildren().add(ro);
    }

    private void updateButtons() {
        if (game.canCheck()) {
            callorcheckButton.setText("Check");
        } else if (game.canCall()){
            callorcheckButton.setText("Call");
        }
        else {
            callorcheckButton.setText("All In");
        }
    }

    private void updateCurrentPlayerInfo() {
        Player current = game.getCurrentPlayer();
        currentPlayer.setText("Current Player: " + current.getName());
        playerStats.setText(current.getName() + " | Chips: " + current.getChips() + "| Current Bet: "
                + game.getCurrent() + "| Pot: " + game.getPot());

        // need to make private...:( im lazy so fix later!!
        playerHand.setText("Your Hand: " + current.getCardOne().getName() + "| " + current.getCardTwo().getName());
        StringJoiner middle = new StringJoiner("| "); // ? need to fix cuz we didn't learn abt stringjoiners yet
        for (Card c : game.getMiddleCards()) {
            middle.add(c.getName());
        }
        if (game.getMiddleCards().isEmpty())
            cards.setText("No community cards so far");
        else
            cards.setText("Community Cards: " + middle.toString());

    }

    //
    // later just stuff all update() methods into here and just call this every time
    // instead of all the stuff individually
    //
    private boolean winnershown = false;

    private void refreshUI() {
        updateCurrentPlayerInfo();
        updateActionState();
        updateButtons();
        if (game.getRoundCount() == 4 && !winnershown) {
            winnershown = true;
            showWinnerScreen();
        }
    }

    // this is easier than 52 else if statements, just title the images as rank of
    // suit.png
    public Image getCards(Card card) {
        Image placeholder = new Image("/redman.png"); // placeholder
        // String fileName = card.getName();
        return placeholder; // later change this to return new Image("/" + fileName + ".png")
    }

    public void loans(){
        ArrayList<Player> players = game.getPlayersReference();
        for (Player p: players){
            if (p.getChips() == 0){
                loan(p);
            }
        }
    }
    public void loan(Player p) {
        rootPane.getChildren().clear();
        Image image = new Image("/loanShark.png");
        Label loanCommentary = new Label(p.getName() + ", wanna borrow some money from Shark?");
        TextField loanField = new TextField();
        loanField.setPromptText("How much money?");
        raiseField.setMaxWidth(120);
        Button yesButton = new Button("yes");
        Button noButton = new Button ("i'd rather not :C");
        Button confirmLoan = new Button("confirm");
        Button cancelLoan = new Button("cancel");
        HBox loanStuff = new HBox(10, loanField, confirmLoan, cancelLoan);
        HBox yesNo = new HBox (10, yesButton, noButton);
        VBox text = new VBox(15, loanCommentary, yesNo, loanStuff);
        HBox loanBox = new HBox(10, text); //add image later to left
        loanBox.setAlignment(Pos.CENTER);
        loanField.setAlignment(Pos.CENTER);
        loanStuff.setVisible(false);
        loanBox.setVisible(true);
        rootPane.getChildren().add(loanBox);
        yesButton.setOnAction(e -> {
            loanStuff.setVisible(true);
        });
        noButton.setOnAction(e -> {
            return;
        });
        confirmLoan.setOnAction(e -> {
            try {
                int amount = Integer.parseInt(loanField.getText());
                if (amount > 5000) {
                    loanCommentary.setText("woah! your credit score isn't high enough to borrow that much money");
                    return;
                }
                if (amount < 0){
                    loanCommentary.setText("so you think you're slick. shark can see right through you");
                    return;
                }
                loanCommentary.setText(p.getName() + " borrowed " + amount);
                p.addChips(amount);
                p.addLoan();
                loanStuff.setVisible(false);
                loanStuff.setManaged(false);
                loanField.clear();
            } catch (Exception ex) {
                loanCommentary.setText("not an integer");
            }
        });
        cancelLoan.setOnAction(e -> {
            loanStuff.setVisible(false);
            loanStuff.setManaged(false);
            loanField.clear();
        });
    }

    public void loan() {
        // identify broke people 
        ArrayList<Player> brokePeople = new ArrayList<>();
        for (Player p : game.getPlayers()){
            if (p.getChips() == 0){
                brokePeople.add(p);
            }
        }
        if (brokePeople.isEmpty()){
            //refreshUI(); // is this how to restart the round? im not sure 
            return;
        }
        // show loan shark for broke people
        for (Player broke: brokePeople){
            Stage loanStage = new Stage();
            loanStage.initModality(Modality.APPLICATION_MODAL);
            loanStage.initStyle(StageStyle.UNDECORATED);
            VBox box = new VBox(16);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(30, 40, 30, 40));
            box.setStyle("-fx-background-color: #b43434; -fx-background-radius: 16; -fx-border-color: #6b3316; -fx-border-width: 3;");
            
            ImageView shark = new ImageView();
            URL sharkURL = getClass().getResource("/loanShark.png");
            shark.setImage(new Image(sharkURL.toExternalForm()));
            shark.setFitHeight(150);
            shark.setPreserveRatio(true);
            Label title = new Label(broke.getName() + " i see ur broke haha");
            Label message = new Label("Oh no! you don't have anymore money... do you want me to loan you some (1000) ??? (if you say no, you will be out of the game :C)");
            message.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
            message.setWrapText(true);
            message.setMaxWidth(300);

            Button confirmButton = new Button("yes");
            confirmButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #46d560");
            Button denyButton = new Button("i'd rather not :C ");
            denyButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #862626");
            confirmButton.setOnAction(e -> {
                broke.addChips(1000);
                refreshUI();
                loanStage.close();
            });
            denyButton.setOnAction(e-> {
                game.setInactive(broke);
                loanStage.close();
            });
             HBox buttons = new HBox(14, confirmButton, denyButton);
            buttons.setAlignment(Pos.CENTER);
 
            box.getChildren().addAll(shark, title, message, buttons);
            Scene sc = new Scene(box, 400, 360);
            sc.setFill(Color.TRANSPARENT);
            loanStage.setScene(sc);
            loanStage.show();
        }
        
    } 
    public boolean canAct() {
        if (game.getPlayers().size() <= 1)
            return false;
        if (game.getRoundCount() >= 4)
            return false;
        return true;
    }

    private void updateActionState() {
        if (game.getPlayers().size() <= 1) {
            showWinnerScreen();
            return;
        }
        callorcheckButton.setDisable(false);
        foldButton.setDisable(false);
        raiseButton.setDisable(false);
    }

    private void showWinnerScreen() {
        rootPane.getChildren().clear();

        VBox endScreen = new VBox(15);
        endScreen.setAlignment(Pos.CENTER);
        endScreen.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 18px;");
        winnerLabel = new Label();
        ArrayList<Player> winners = game.findWinner();
        int[] added = game.splitWinnings();
        String hand = game.handType(winners.get(0));
        if (winners.size() == 1){
            winnerLabel.setText(winners.get(0).getName() +
            " won " + game.getPot() + " chips with a " + hand);
        }
        else{
            boolean diff = false;
            for (int i = 0; i < added.length - 1; i++){
                if (added[i] != added[i+1]){
                    diff = true;
                }
            }
            if (!diff){
                String text = "";
                if (winners.size() == 2){
                    text = winners.get(0).getName() + " and " + winners.get(1).getName(); 
                }
                else{
                    for (int i = 0; i < winners.size() - 1; i++){
                        text += winners.get(i).getName() + ", ";
                    }
                    text += "and ";
                    text += winners.get(winners.size() - 1).getName();
                }
                
                winnerLabel.setText(text + " each won " + added[0] + " chips with a " + hand);
            }
            else{
                String text = "The following players each had a " + hand;
                for (int i = 0; i < winners.size(); i++){
                    text += ("\n" + winners.get(i).getName() + " won " + added[i] + " chips");
                }
                winnerLabel.setText(text);
            }
        }
        winnerLabel.setStyle("-fx-text-fill: white;");
        Button restart = new Button("play again");
        restart.setOnAction(e -> {
            //loans();
            loan();
            game.resetRound();
            pokerTable();
        });
        endScreen.getChildren().addAll(winnerLabel, restart);
        rootPane.getChildren().add(endScreen);
        winnershown = false;
        // Stage stage = (Stage) callorcheckButton.getScene().getWindow();
        // stage.getScene().setRoot(rootPane);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private StackPane createBackgroundPane() {
        StackPane rootPane = new StackPane();
        BackgroundFill fallbackColor = new BackgroundFill(
                Color.web("#1a3a1a"),
                CornerRadii.EMPTY,
                Insets.EMPTY);
        rootPane.setBackground(new Background(fallbackColor));

        URL bgUrl = getClass().getResource("/pokerbackground.png");
        Image bgImage = new Image(bgUrl.toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true));
        rootPane.setBackground(
                new Background(new BackgroundFill[] { fallbackColor }, new BackgroundImage[] { backgroundImage }));
        return rootPane;
    }

    private void lockWindowRatio(Stage stage) {
        final boolean[] resizing = { false };

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (resizing[0] || !stage.isShowing())
                return;
            resizing[0] = true;
            stage.setHeight(newWidth.doubleValue() / WINDOW_RATIO);
            resizing[0] = false;
        });

        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            if (resizing[0] || !stage.isShowing())
                return;
            resizing[0] = true;
            stage.setWidth(newHeight.doubleValue() * WINDOW_RATIO);
            resizing[0] = false;
        });
    }
}
