package com.example;

import java.net.URL;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PokerFX extends Application {

    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 600;
    private static final double WINDOW_RATIO = WINDOW_WIDTH / WINDOW_HEIGHT;

    private Game game;
    private Label playerStats;
    private Label currentPlayer;
    private Button callorcheckButton, foldButton, raiseButton;
    private Label playerHand;
    private Label cards;
    private MediaPlayer mediaPlayer;
    // private TextField raiseField;
    private Label raiseAmount;
    private Slider raiseSlider;
    private HBox raiseBox;
    private Label winnerLabel;
    private Label dealerCommentary;
    private StackPane rootPane;
    private HBox cardBox;
    private VBox enlargedHand;
    private HBox bottomBox;
    private HBox communityBox;
    private VBox potBox;
    private Label potLabel;
    private StackPane stack1;
    private StackPane stack10;
    private StackPane stack100;
    private StackPane stack1000;
    private ImageView chip1;
    private ImageView chip10;
    private ImageView chip100;
    private ImageView chip1000;
    private java.util.List<VBox> seatBoxes;
    private Label communityLabel;
    private VBox pBox;
    private HBox potBottom;
    private HBox potTop;
    private Button flip;
    private Button flipb;
    private static String[][] musicSelection = {
        {"Relaxing Jazz", "/relaxingJazz.mp3"}, {"Less Relaxing Jazz", "/lessRelaxingJazz.mp3"}, {"Lesser Relaxing Jazz", "/lesserRelaxingJazz.mp3"},{"shrimp's Jazz", "/shrimpsJazz.mp3"},
    };
    private int selectedJazz = 0;

    private void playBeautifulJazzMusic(int track){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        if (track < 0) return;
        URL musicUrl = getClass().getResource(musicSelection[track][1]);
        if (musicUrl != null){
            Media media = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //loop forever
            mediaPlayer.play();
        }
    }

    @Override
    public void start(Stage stage) {
        // for background
        rootPane = createBackground();
        // for music
        // start game
        Label intro = new Label("Welcome to Poker!");
        intro.setStyle("-fx-text-fill: white;");
        Label prompt = new Label("How many players? ");
        prompt.setStyle("-fx-text-fill: white;");
        TextField numberOfPlayers = new TextField();
        numberOfPlayers.setPromptText("2 - 6 players");
        numberOfPlayers.setMaxWidth(125);

        Label musicLabel = new Label(" Choose ur music! ");
        musicLabel.setStyle("-fx-text-fill: white;");
        HBox trackBox = new HBox(8);
        trackBox.setAlignment(Pos.CENTER);
        Button[] trackButtons = new Button[4];
        for (int i = 0; i < 4; i++){
            final int index = i;
            Button button = new Button(musicSelection[i][0]);
            button.setOnAction(e -> {
                selectedJazz = index;
                playBeautifulJazzMusic(index);
            });
            trackButtons[i] = button;
            trackBox.getChildren().add(button);
        }
        playBeautifulJazzMusic(selectedJazz);

        Button startGame = new Button("Start");
        dealerCommentary = new Label("Starting Game!");
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:transparent; -fx-padding: 20; -fx-font-size: 18px;");
        root.getChildren().addAll(intro, prompt, numberOfPlayers, startGame, musicLabel, trackBox);
        rootPane.getChildren().add(root);

        Scene scene = new Scene(rootPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Poker");
        Image icon = new Image("poker symbol.png");
        stage.getIcons().add(icon);
        //stage.setResizable(false);
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
        chip1 = new ImageView("/1chip.png");
        chip10 = new ImageView("/10chip.png");
        chip100 = new ImageView("/100chip.png");
        chip1000 = new ImageView("/1000chip.png");
    }

    // below this is the gameboard , above this is getting the names and players
    private void pokerTable() {
        if (game.getPlayers().size() <= 1) {
            dealerCommentary.setText("Not enough players to continue. Thanks for playing!");
            endGame();
            return;
        }
        // commentary for dealer shark
        HBox topCommentary = new HBox(10);
        topCommentary.setAlignment(Pos.TOP_CENTER);
        topCommentary.setPadding(new Insets(15, 40, 6, 40));
        topCommentary.setStyle("-fx-background-color: transparent;");

        ImageView dealerImage = new ImageView();
        URL dealerURL = getClass().getResource("/dealer (1) (1).png");
        dealerImage.setImage(new Image(dealerURL.toExternalForm()));
        dealerImage.setFitHeight(120);
        dealerImage.setPreserveRatio(true);

        javafx.scene.shape.Rectangle bubble = new javafx.scene.shape.Rectangle(200, 40);
        bubble.setArcWidth(18);
        bubble.setArcHeight(18);
        bubble.setFill(Color.SKYBLUE);

        dealerCommentary.setStyle("-fx-text-fill: black;");
        dealerCommentary.setMaxWidth(200);
        dealerCommentary.setWrapText(true);
        dealerCommentary.setAlignment(Pos.CENTER);
        dealerCommentary.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        dealerCommentary.translateYProperty().setValue((WINDOW_HEIGHT - dealerImage.getImage().getHeight()) / 2 - 50);

        StackPane speechBubble = new StackPane(bubble, dealerCommentary);
        speechBubble.setStyle("-fx-background-color: transparent;");
        // speechBubble.getChildren().addAll(bubble, dealerCommentary);
        speechBubble.setAlignment(Pos.TOP_CENTER);
        speechBubble.translateYProperty().setValue((WINDOW_HEIGHT - dealerImage.getImage().getHeight()) / 2);
        speechBubble.setTranslateY(speechBubble.getTranslateY() - 10);

        topCommentary.getChildren().addAll(dealerImage, speechBubble);

        // player seating
      //  VBox playerSeat = new VBox(6);
       // playerSeat.setAlignment(Pos.CENTER_LEFT);
      //  playerSeat.setPadding(new Insets(10, 14, 10, 14));
      //  playerSeat.setStyle(
      //          "-fx-background-color: #a1bcc4; -fx-background-radius: 10; -fx-border-color: #6d6448; -fx-border-radius: 10;");
      //  playerSeat.setMaxWidth(200);

        playerStats = new Label("Stats over here");
        playerStats.setStyle("-fx-text-fill: white;");
        playerStats.setWrapText(true);

        seatBoxes = new ArrayList<>();
        VBox leftBoxes = new VBox(8);
        leftBoxes.setAlignment(Pos.CENTER_LEFT);
        leftBoxes.setPadding(new Insets(8,6,8,6));
        VBox rightBoxes = new VBox(8);
        rightBoxes.setAlignment(Pos.CENTER_RIGHT);
        rightBoxes.setPadding(new Insets(8,6,8,6));
        potLabel = new Label();
        for (int i = 0; i < 6; i++){
            VBox playerSeat = playerSeatBox(i);
            seatBoxes.add(playerSeat);
            if (i < 3){
                leftBoxes.getChildren().add(playerSeat);
            } else {
                rightBoxes.getChildren().add(playerSeat);
            }
        }
        
        HBox middleBox = new HBox(20);
        VBox commBox = new VBox(6);
        pBox = new VBox(10);
        commBox.setAlignment(Pos.CENTER);
        communityLabel = new Label("");
        communityLabel.setWrapText(true);
        communityLabel.setStyle("-fx-text-fill: white");
        communityBox = new HBox(6);
        communityBox.setAlignment(Pos.CENTER);
        cards = new Label();
        cards.setVisible(false);
        cards.setManaged(false);
        potLabel = new Label(); // how many chips in the pot
        potLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        potLabel.setAlignment(Pos.CENTER);
        potBox = new VBox(10);
        stack1 = new StackPane();
        stack10 = new StackPane();
        stack100 = new StackPane();
        stack1000 = new StackPane();
        potBottom = new HBox(5);
        potTop = new HBox(5);
        potBox.getChildren().addAll(potTop, potBottom);
        stack1.setManaged(false); 
        stack10.setManaged(false);
        stack100.setManaged(false);
        stack1000.setManaged(false);
        potBox.setAlignment(Pos.CENTER);
        pBox.getChildren().addAll(potBox, potLabel);
        pBox.setAlignment(Pos.CENTER);
        commBox.getChildren().addAll(communityLabel, communityBox);
        communityLabel.setManaged(false);
        middleBox.getChildren().addAll(pBox, commBox);
        middleBox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(commBox, Pos.CENTER);
        StackPane.setAlignment(communityBox, Pos.CENTER);
        StackPane.setAlignment(middleBox, Pos.CENTER);

        // current player's hand + control buttons
        VBox bottomControls = new VBox(8);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setPadding(new Insets(8, 20, 12, 20));
        currentPlayer = new Label();
        playerHand = new Label("Your Hand");
        playerHand.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        callorcheckButton = new Button("Call");
        foldButton = new Button("Fold");
        raiseButton = new Button("Raise");
        raiseSlider = new Slider(2, game.getCurrentPlayer().getChips(), 50);
        raiseSlider.setShowTickMarks(true);
        raiseSlider.setShowTickLabels(true);
        raiseSlider.setMajorTickUnit(100);
        raiseSlider.setMinorTickCount(1);
        raiseSlider.setPrefWidth(650);
        raiseSlider.setBlockIncrement(25); // arrow keys make it go 25
        raiseSlider.setStyle("-fx-text-fill: white;"); // arrow keys make it go 25
        raiseAmount = new Label("50");
        raiseAmount.setStyle("-fx-text-fill: white;");
        raiseSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            raiseAmount.setText(String.valueOf(newVal.intValue()));
        });
        // raiseField = new TextField();
        // raiseField.setPromptText("raise amount");
        // raiseField.setMaxWidth(120);

        Button confirmRaise = new Button("confirm");
        Button cancelRaise = new Button("cancel");

        raiseBox = new HBox(10, raiseAmount, raiseSlider, confirmRaise, cancelRaise);
        raiseBox.setAlignment(Pos.CENTER);
        raiseBox.setVisible(false);
        raiseBox.setManaged(false);

        foldButton.setOnAction(e -> {
            if (!canAct())
                return;
            dealerCommentary.setText(game.getCurrentPlayer().getName() + " folded");
            game.fold();
            refreshUI();
        });

        callorcheckButton.setOnAction(e -> {
            if (!canAct())
                return;
            if (game.canCheck()) {
                dealerCommentary.setText(game.getCurrentPlayer().getName() + " checked");
                game.check();
            } else if (game.canCall() && game.getAmountToCall() != game.getCurrentPlayer().getChips()) {
                dealerCommentary.setText(game.getCurrentPlayer().getName() + " called");
                game.call();
            } else {
                dealerCommentary.setText(game.getCurrentPlayer().getName() + " went all in!");
                game.call();
            }
            refreshUI();
        });

        raiseButton.setOnAction(e -> {
            raiseBox.setVisible(true);
            raiseBox.setManaged(true);
            callorcheckButton.setDisable(true);
            foldButton.setDisable(true);
            enlargedHand.setVisible(false);
            enlargedHand.setManaged(false);
        });
        confirmRaise.setOnAction(e -> {
            if (!canAct())
                return;
            try {
                int amount = (int) raiseSlider.getValue();
                if (!game.canRaiseBy(amount + game.getAmountToCall())) {
                    dealerCommentary.setText("ur too broke");
                    return;
                }
                if (amount + game.getAmountToCall() >= game.getCurrentPlayer().getChips()) {
                    dealerCommentary.setText(game.getCurrentPlayer().getName() + " went all in with a raise!");
                } else {
                    dealerCommentary.setText(game.getCurrentPlayer().getName() + " raised by " + amount);
                }
                game.raise(amount);
                raiseBox.setVisible(false);
                raiseBox.setManaged(false);
                callorcheckButton.setDisable(false);
                foldButton.setDisable(false);
                enlargedHand.setVisible(true);
                enlargedHand.setManaged(true);
                refreshUI();
            } catch (Exception ex) {
                dealerCommentary.setText("not valid");
            }
        });
        cancelRaise.setOnAction(e -> {
            raiseBox.setVisible(false);
            raiseBox.setManaged(false);
            callorcheckButton.setDisable(false);
            foldButton.setDisable(false);
            enlargedHand.setVisible(true);
            enlargedHand.setManaged(true);
        });
        cardBox = new HBox(6);
        cardBox.setAlignment(Pos.CENTER);
        flip = new Button();
        flipb = new Button();
        updateCurrentPlayerInfo();

        controls.getChildren().addAll(callorcheckButton, foldButton, raiseButton);

        flip = new Button("Show Cards");

        flip.setOnAction(e -> {
            ImageView cardOnee = getCards(game.getCurrentPlayer().getCardOne());
            ImageView cardTwoo = getCards(game.getCurrentPlayer().getCardTwo());
            cardOnee.setFitHeight(80);
            cardOnee.setPreserveRatio(true);
            cardTwoo.setFitHeight(80);
            cardTwoo.setPreserveRatio(true);
            cardBox.getChildren().clear();
            cardBox.getChildren().addAll(cardOnee, cardTwoo);
            flip.setVisible(false);
            flip.setManaged(false);
            enlargedHand.getChildren().clear();
            enlargedHand.getChildren().addAll(playerHand, cardBox);
            flipb = new Button("Hide Cards");
            flipb.setOnAction(evv -> {
                ImageView cardOne = new ImageView("/cardBack.png");
                ImageView cardTwo = new ImageView("/cardBack.png");
                cardOne.setFitHeight(80);
                cardOne.setPreserveRatio(true);
                cardTwo.setFitHeight(80);
                cardTwo.setPreserveRatio(true);
                cardBox.getChildren().clear();
                cardBox.getChildren().addAll(cardOne, cardTwo);
                flipb.setVisible(false);
                flipb.setManaged(false);
                flip.setVisible(true);
                flip.setManaged(true);
            });
            bottomControls.getChildren().add(flipb);
        });
        enlargedHand = new VBox(6);
        bottomBox = new HBox(20);
        enlargedHand.getChildren().addAll(playerHand, cardBox);
        playerHand.setText(game.getCurrentPlayer().getName() + "'s Hand");
        playerHand.setTextAlignment(TextAlignment.CENTER);
        playerHand.setAlignment(Pos.CENTER);
        bottomControls.getChildren().addAll(currentPlayer, controls, raiseBox, flip);
        bottomBox.getChildren().addAll(enlargedHand, bottomControls);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.translateYProperty().setValue(-20);
        javafx.scene.layout.BorderPane table = new javafx.scene.layout.BorderPane();
        table.setStyle("-fx-background-color:transparent;");
        table.setTop(topCommentary);
        middleBox.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.layout.BorderPane.setAlignment(middleBox, Pos.CENTER);
        table.setCenter(middleBox);
        table.setBottom(bottomBox);
        table.setLeft(leftBoxes);
        table.setRight(rightBoxes);
        rootPane.getChildren().clear();
        rootPane.getChildren().add(table);
        callorcheckButton.setDisable(true);
        foldButton.setDisable(true);
        raiseButton.setDisable(true);
        flip.setDisable(true);
        int[] indices = game.getBlindIndices();
        int small = indices[0];
        int big = indices[1];
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        ArrayList<Player> players = game.getPlayers();
        pause.setOnFinished(event -> {
            int[] smallUpdates = players.get(small).bet(game.getSmallBlind());
            game.addToPot(smallUpdates[1]);
            dealerCommentary.setText(game.getPlayers().get(small).getName() + " paid the small blind of " + game.getSmallBlind());
            game.setCurrentPlayer(small);
            updateCurrentPlayerInfo();
            updateSeats();
            raiseButton.setDisable(true);
            PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
            pause2.setOnFinished(event2 -> {
                int[] bigUpdates = players.get(big).bet(game.getBigBlind());
                game.addToPot(bigUpdates[1]);
                dealerCommentary.setText(game.getPlayers().get(big).getName() + " paid the big blind of " + game.getBigBlind());
                game.setCurrentPlayer(big);
                updateCurrentPlayerInfo();
                updateSeats();
                raiseButton.setDisable(true);
                PauseTransition pause3 = new PauseTransition(Duration.seconds(2));
                pause3.setOnFinished(event3 -> {
                    game.setFirstPlayer();
                    updateCurrentPlayerInfo();
                    updateSeats();
                    dealerCommentary.setText(game.getCurrentPlayer().getName() + ", you're up first!");
                    callorcheckButton.setDisable(false);
                    foldButton.setDisable(false);
                    raiseButton.setDisable(false);
                    flip.setDisable(false);
                });
                pause3.play();
            });
            pause2.play();
        });
        pause.play();
    }

    private void updateButtons() {
        if (game.canCheck()) {
            callorcheckButton.setText("Check");
        } else if (game.canCall()) {
            callorcheckButton.setText("Call");
        } else {
            callorcheckButton.setText("All In");
        }
    }

    private void updateCurrentPlayerInfo() {
        Player current = game.getCurrentPlayer();
        raiseSlider.setMax(Math.max(current.getChips() - game.getAmountToCall(), 0));
        raiseSlider.setValue(50);
        raiseButton.setDisable(current.getChips() <= 0);
        currentPlayer.setText("Current Player: " + current.getName());
        currentPlayer.setTextFill(Color.SKYBLUE);
        playerStats.setText(current.getName() + " | Chips: " + current.getChips() + "| Current Bet: "
                + game.getCurrent() + "| Pot: " + game.getPot());


        communityBox.getChildren().clear();
        if (game.getMiddleCards().isEmpty()) {
            Label noCards = new Label("No community cards yet!");
            noCards.setStyle("-fx-text-fill:white; -fx-font-size: 13px");
            communityBox.getChildren().add(noCards);
        } else {
            communityLabel.setText("Community Cards");
            communityLabel.setManaged(true);
            for (Card c : game.getMiddleCards()) {
                ImageView cardd = getCards(c);
                cardd.setFitHeight(90);
                cardd.setPreserveRatio(true);
                communityBox.getChildren().add(cardd);
            }
        }
        updatePot();

        ImageView cardBack1 = new ImageView("/cardBack.png");
        ImageView cardBack2 = new ImageView("/cardBack.png");
        cardBack1.setFitHeight(80);
        cardBack1.setPreserveRatio(true);
        cardBack2.setFitHeight(80);
        cardBack2.setPreserveRatio(true);
        cardBox.getChildren().clear();
        cardBox.getChildren().addAll(cardBack1, cardBack2);
        flipb.setVisible(false);
        flipb.setManaged(false);
        flip.setVisible(true);
        flip.setManaged(true);
    }

    private void updatePot(){
        int pot = game.getPot();
        if (pot == 1){
            potLabel.setText(pot + " chip");
        }
        else potLabel.setText(pot + " chips");
        int num1000 = pot / 1000;
        pot -= num1000 * 1000;
        int num100 = pot / 100;
        pot -= num100 * 100;
        int num10 = pot / 10;
        pot -= num10 * 10;
        int num1 = pot;
        stack1000.getChildren().clear();
        stack100.getChildren().clear();
        stack10.getChildren().clear();
        stack1.getChildren().clear();
        potBottom.getChildren().clear();
        potTop.getChildren().clear();
        ArrayList<StackPane> visibleStacks = new ArrayList<>();
        int chipHeight = 50;
        int offset = 4;
        if (num1000 > 0){
            stack1000.setManaged(true);
            visibleStacks.add(stack1000);
            generateStack(stack1000, chip1000, num1000, chipHeight, offset);
        }
        else {
            stack1000.setManaged(false);
        }
        if (num100 > 0){
            stack100.setManaged(true);
            visibleStacks.add(stack100);
            generateStack(stack100, chip100, num100, chipHeight, offset);
        }
        else {
            stack100.setManaged(false);
        }  
        if (num10 > 0){
            stack10.setManaged(true);
            visibleStacks.add(stack10);
            generateStack(stack10, chip10, num10, chipHeight, offset);
        }
        else {
            stack10.setManaged(false);
        }
        if (num1 > 0){
            visibleStacks.add(stack1);
            stack1.setManaged(true);
            generateStack(stack1, chip1, num1, chipHeight, offset);
        }
        else {
            stack1.setManaged(false);
        }
        potLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        for (int i = 0; i < visibleStacks.size(); i++){
            if (i < 2){
                potBottom.getChildren().add(visibleStacks.get(i));
            }
            else {
                potTop.getChildren().add(visibleStacks.get(i));
            }
        }
        potBox.setAlignment(Pos.CENTER);
        pBox.setAlignment(Pos.CENTER);

    }
    private void generateStack(StackPane stack, ImageView chip, int amount, int height, double offset){
        for (int i = 0; i < amount; i++){
            ImageView chipCopy = new ImageView(chip.getImage());
            chipCopy.setFitHeight(height);
            chipCopy.setPreserveRatio(true);
            chipCopy.setTranslateY(-i * offset);
            stack.getChildren().add(chipCopy);
        }
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
        updateSeats();
        if (game.getRoundCount() == 4 && !winnershown) {
            winnershown = true;
            showWinnerScreen();
        }
    }

    // seats for each place
    private VBox playerSeatBox(int i){
       VBox seat = new VBox(28);
       if (i < 3){
            seat.setAlignment(Pos.CENTER_LEFT);
       } else {
        seat.setAlignment(Pos.CENTER_RIGHT);
       }
       seat.setPadding(new Insets(8,10,8,10));
       seat.setMinWidth(150);
       seat.setMaxWidth(150);
       updateSeatBox(seat, i);
       return seat;
    }

    private void updateSeatBox(VBox seat, int i){
        seat.getChildren().clear();
        java.util.List<Player> players = game.getOverallPlayers();

        if (i >= players.size()){
            seat.setStyle("-fx-background-color: #b15050; -fx-background-radius: 10;");
            Label emptySeat = new Label("Empty Seat");
            if (i < 3){
                emptySeat.setAlignment(Pos.CENTER_LEFT);
            } else {
                emptySeat.setAlignment(Pos.CENTER_RIGHT);
            }
            emptySeat.setStyle("-fx-text-fill: #ffe7e7; -fx-font-size: 11px;");
            emptySeat.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            seat.getChildren().add(emptySeat);
            return;
        }
        Player p = players.get(i);
        boolean isCurrentPlayer = p.equals(game.getCurrentPlayer());
        boolean isInGame = game.getPlayers().contains(p);
        String background;
        String border;
        if (isInGame){
            background = "#b0d6bd";
            border = "#3f855d";
            if (isCurrentPlayer){
                background = "#95bcef";
                border = "#5458a9";
            }
            seat.setStyle("-fx-background-color: " + background + "; -fx-background-radius: 10; -fx-border-color: " + border + "; -fx-border-width: 2;");
        } 
        else{
            background = "#b0d6bd4b";
            border = "#3f855d97";
            seat.setStyle("-fx-background-color: " + background + "; -fx-background-radius: 10; -fx-border-color: " + border + "; -fx-border-width: 2;");
        }

        Label playerNameAndChips;
        if (isInGame){
            playerNameAndChips = new Label(p.getName() + ": " + p.getChips() + " chips");
        } else {
            playerNameAndChips = new Label(p.getName() + ": folded");
        }
        playerNameAndChips.setStyle("-fx-text-fill: black; -fx-font-size: 11px; ");
        
        
        seat.getChildren().add(playerNameAndChips);
        playerNameAndChips.setWrapText(true);
        playerNameAndChips.setAlignment(Pos.CENTER);
        playerNameAndChips.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        int chips = game.getOverallPlayers().get(i).getChips();
        int num1000 = chips / 1000;
        chips -= num1000 * 1000;
        int num100 = chips / 100;
        chips -= num100 * 100;
        int num10 = chips / 10;
        chips -= num10 * 10;
        int num1 = chips;
        StackPane stac1000 = new StackPane();
        StackPane stac100 = new StackPane();
        StackPane stac10 = new StackPane();
        StackPane stac1 = new StackPane();
        HBox chipBox = new HBox(2);
        chipBox.setAlignment(Pos.CENTER);
        int chipHeight = 30;
        double offset = 2;
        if (num1000 > 0){
            chipBox.getChildren().add(stac1000);
            generateStack(stac1000, chip1000, num1000, chipHeight, offset);
        }
        if (num100 > 0){
            chipBox.getChildren().add(stac100);
            generateStack(stac100, chip100, num100, chipHeight, offset);
        }
        if (num10 > 0){
            chipBox.getChildren().add(stac10);
            generateStack(stac10, chip10, num10, chipHeight, offset);
        }
        if (num1 > 0){
            chipBox.getChildren().add(stac1);
            generateStack(stac1, chip1, num1, chipHeight, offset);
        }
        potLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        seat.getChildren().add(chipBox);
    }

    private void updateSeats(){
        if (seatBoxes == null){
            return;
        } 
        for (int i = 0; i < 6; i++){
            updateSeatBox(seatBoxes.get(i), i);
        }
    }
    // this is easier than 52 else if statements, just title the images as rank of
    // suit.png
    public ImageView getCards(Card card) {
        ImageView cards = new ImageView();
        String filename = "/" + String.valueOf(card.getRank()) + "Of" + card.getSuit().toString() + ".png";
        URL cardsURL = getClass().getResource(filename);
        cards.setImage(new Image(cardsURL.toExternalForm()));
        return cards;
    }

    public void loan() {
        // identify broke people
        ArrayList<Player> brokePeople = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            if (p.getChips() == 0) {
                brokePeople.add(p);
            }
        }
        if (brokePeople.isEmpty()) {
            // game.resetRound();
            // return;
        }
        final int[] numDroppedOut = new int[] { 0 };
        // show loan shark for broke people
        for (Player broke : brokePeople) {

            Stage loanStage = new Stage();
            VBox box = new VBox(16);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(30, 40, 30, 40));
            box.setStyle(
                    "-fx-background-color: #b96767; -fx-background-radius: 16; -fx-border-color: #6b3316; -fx-border-width: 3;");

            ImageView shark = new ImageView();
            URL sharkURL = getClass().getResource("/loanShark.png");
            shark.setImage(new Image(sharkURL.toExternalForm()));
            shark.setFitHeight(150);
            shark.setPreserveRatio(true);
            Label title = new Label(broke.getName() + " ur broke haha, wana borrow sum money from Shark?? 🥺🥺");
            Label message = new Label(
                    "do you want me to loan you some chips (1000) ?? (no = out of the game :C)");
            message.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
            message.setWrapText(true);
            message.setMaxWidth(300);

            Button confirmButton = new Button("yes");
            confirmButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #46d560");
            Button denyButton = new Button("i'd rather not :C ");
            denyButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #862626");
            confirmButton.setOnAction(e -> {
                broke.addChips(1000);
                broke.addLoan();
                loanStage.close();
            });
            denyButton.setOnAction(e -> {
                game.setInactive(broke);
                numDroppedOut[0]++;
                loanStage.close();
            });
            HBox buttons = new HBox(14, confirmButton, denyButton);
            buttons.setAlignment(Pos.CENTER);

            box.getChildren().addAll(shark, title, message, buttons);
            Scene sc = new Scene(box, 400, 360);
            sc.setFill(Color.TRANSPARENT);
            loanStage.setScene(sc);
            loanStage.showAndWait();
        }
        if (game.getPlayersReference().size() <= 1) {
            System.out.println("reached");
            dealerCommentary.setText("Not enough players to continue. Thanks for playing!");
            endGame();
            return;
        } else {
            game.resetRound();
            dealerCommentary.setText("Starting new round!");
            pokerTable();
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
        if (winners.size() == 1) {
            winnerLabel.setText(winners.get(0).getName() +
                    " won " + game.getPot() + " chips with a " + hand);
        } else {
            boolean diff = false;
            for (int i = 0; i < added.length - 1; i++) {
                if (added[i] != added[i + 1]) {
                    diff = true;
                }
            }
            if (!diff) {
                String text = "";
                if (winners.size() == 2) {
                    text = winners.get(0).getName() + " and " + winners.get(1).getName();
                } else {
                    for (int i = 0; i < winners.size() - 1; i++) {
                        text += winners.get(i).getName() + ", ";
                    }
                    text += "and ";
                    text += winners.get(winners.size() - 1).getName();
                }

                winnerLabel.setText(text + " each won " + added[0] + " chips with a " + hand);
            } else {
                String text = "The following players each had a " + hand;
                for (int i = 0; i < winners.size(); i++) {
                    text += ("\n" + winners.get(i).getName() + " won " + added[i] + " chips");
                }
                winnerLabel.setText(text);
            }
        }
        winnerLabel.setText(winnerLabel.getText() + "\n");
        winnerLabel.setStyle("-fx-text-fill: white;");
        dealerCommentary.setText("Wanna play another round?");
        dealerCommentary.setStyle("-fx-text-fill: white;");
        dealerCommentary.setMaxWidth(WINDOW_WIDTH * 0.6);
        HBox buttonBox = new HBox(10);
        Button restart = new Button("yes!");
        Button no = new Button("no :(");
        buttonBox.getChildren().addAll(restart, no);
        buttonBox.setAlignment(Pos.CENTER);
        restart.setOnAction(e -> {
            // loans();
            loan();
            // game.resetRound();

        });
        no.setOnAction(e -> {
            endGame();
        });
        endScreen.getChildren().addAll(winnerLabel, dealerCommentary, buttonBox);
        rootPane.getChildren().add(endScreen);
        winnershown = false;
        // Stage stage = (Stage) callorcheckButton.getScene().getWindow();
        // stage.getScene().setRoot(rootPane);
    }

    public void endGame() {
        rootPane.getChildren().clear();
        if (!dealerCommentary.getText().contains("Not enough players")) {
            dealerCommentary.setText("Thanks for playing!\n"); // if we ended game bc not enough players, don't change
                                                               // text
        }
        playerStats.setText("\nFinal Player Stats:\n");
        ArrayList<String> loanPlayers = new ArrayList<>();
        for (Player p : game.getOverallPlayers()) {
            playerStats.setText(playerStats.getText() + p.getName() + " | Chips: " + p.getChips()
                    + " | Number of loans: " + p.getLoans() + "\n");
            if (p.getLoans() > 0) {
                loanPlayers.add(p.getName());
            }
        }
        Label loanLabel = new Label();
        if (loanPlayers.size() == 1){
            loanLabel.setText(loanPlayers.get(0) + " owes Shark. Shark has his eyes on you...");
        } else if (loanPlayers.size() == 2){
            loanLabel.setText(loanPlayers.get(0) + " and " + loanPlayers.get(1) + " owes Shark. Shark has his eyes on you...");
        }
        else if (loanPlayers.size() > 2){
            String text = "";
            for (int i = 0; i < loanPlayers.size() - 1; i++){
                text += loanPlayers.get(i) + ", ";
            }
            text += "and " + loanPlayers.get(loanPlayers.size() - 1) + " owes Shark. Shark has his eyes on you...";
            loanLabel.setText(text);
        }
        loanLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        playerStats.setAlignment(Pos.CENTER);
        Button credits = new Button("Credits & stuff");
        credits.setOnAction(e -> {
            credits();
        });
        credits.setAlignment(Pos.CENTER);
        playerStats.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        playerStats.setTextAlignment(TextAlignment.CENTER);
        dealerCommentary.setStyle("-fx-text-fill: white; -fx-font-size: 17px;");
        HBox topCommentary = new HBox(10);
        topCommentary.setAlignment(Pos.TOP_CENTER);
        topCommentary.setPadding(new Insets(15, 40, 6, 40));
        topCommentary.setStyle("-fx-background-color: transparent;");

        ImageView dealerImage = new ImageView();
        URL dealerURL = getClass().getResource("/dealer (1) (1).png");
        dealerImage.setImage(new Image(dealerURL.toExternalForm()));
        dealerImage.setFitHeight(120);
        dealerImage.setPreserveRatio(true);

        javafx.scene.shape.Rectangle bubble = new javafx.scene.shape.Rectangle(200, 40);
        bubble.setArcWidth(18);
        bubble.setArcHeight(18);
        bubble.setFill(Color.SKYBLUE);

        dealerCommentary.setStyle("-fx-text-fill: black;");
        dealerCommentary.setMaxWidth(200);
        dealerCommentary.setWrapText(true);
        dealerCommentary.setAlignment(Pos.CENTER);
        dealerCommentary.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        dealerCommentary.translateYProperty().setValue((WINDOW_HEIGHT - dealerImage.getImage().getHeight()) / 2 - 50);
        

        StackPane speechBubble = new StackPane(bubble, dealerCommentary);
        speechBubble.setStyle("-fx-background-color: transparent;");
        // speechBubble.getChildren().addAll(bubble, dealerCommentary);
        speechBubble.setAlignment(Pos.TOP_CENTER);
        speechBubble.translateYProperty().setValue((WINDOW_HEIGHT - dealerImage.getImage().getHeight()) / 2);
        speechBubble.setTranslateY(speechBubble.getTranslateY() - 10);
        if (dealerCommentary.getText().contains("Not enough players")) {
            dealerCommentary.setTranslateY(dealerCommentary.getTranslateY() - 5);
        }

        topCommentary.getChildren().addAll(dealerImage, speechBubble);
        VBox wholeBox = new VBox(20);
        wholeBox.getChildren().add(playerStats);
        if (loanPlayers.size() > 0){
            wholeBox.getChildren().add(loanLabel);
        }
        wholeBox.getChildren().add(credits);
        wholeBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(topCommentary, wholeBox);
    }

    public void credits(){
        rootPane.getChildren().clear();
        rootPane.setStyle("-fx-background-color: black;");
        VBox text = new VBox(10);
        text.setAlignment(Pos.CENTER);


        Label thanks = new Label("THANK YOU FOR SUPPORTING SHARK'S BUSINESS!");
        thanks.setStyle("-fx-text-fill: #ffce2c; -fx-font: 30px 'Courier New'; ");

        Label creators = new Label("Created by");
        creators.setStyle("-fx-text-fill: #ffce2c; -fx-font: 13px 'Courier New'; ");

        Label names = new Label("Anna Chen ~ Sophia Fan ~ Vicky Qin");
        names.setStyle("-fx-text-fill: #fcf2d2; -fx-font: 18px 'Courier New'; ");

        Label music = new Label("Music");
        music.setStyle("-fx-text-fill: #ffce2c; -fx-font: 13px 'Courier New'; ");
        
        Label song1 = new Label("just business, darling. by is it sunday?");
        Label song2 = new Label("Casino music for your gambling session || Playlist by SaraCrossing");
        Label song3 = new Label("jazz... but it's SHRIMP by me time.");
        Label song4 = new Label("Whispers & Jazz -- Vintage 1940's Noir Jazz for Relaxation by Pink Panther's Lounge");
        song1.setStyle("-fx-text-fill: #fcf2d2; -fx-font: 18px 'Courier New'; ");
        song2.setStyle("-fx-text-fill: #fcf2d2; -fx-font: 18px 'Courier New'; ");
        song3.setStyle("-fx-text-fill: #fcf2d2; -fx-font: 18px 'Courier New'; ");
        song4.setStyle("-fx-text-fill: #fcf2d2; -fx-font: 18px 'Courier New'; ");

        ImageView sharksGoodbye = new ImageView();
        URL goodbyeUrl = getClass().getResource("/sharksGoodbye.png");
        sharksGoodbye.setImage(new Image(goodbyeUrl.toExternalForm()));

        ImageView sharksFace = new ImageView();
        URL faceUrl = getClass().getResource("/sharkEnding.png");
        sharksFace.setImage(new Image(faceUrl.toExternalForm()));
        sharksFace.setFitHeight(100);
        sharksFace.setPreserveRatio(true);

        text.getChildren().addAll(thanks, creators, names, music, song1, song2, song3, song4, sharksGoodbye, sharksFace);
        rootPane.getChildren().add(text);
        }

    public static void main(String[] args) {
        launch(args);
    }

    private StackPane createBackground() {
        StackPane rootPane = new StackPane();
        BackgroundFill backColor = new BackgroundFill(
                Color.web("#1a3a1a"),
                CornerRadii.EMPTY,
                Insets.EMPTY);
        rootPane.setBackground(new Background(backColor));

        URL bgUrl = getClass().getResource("/pokerbackground1.png");
        Image bgImage = new Image(bgUrl.toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true));
        rootPane.setBackground(
                new Background(new BackgroundFill[] { backColor }, new BackgroundImage[] { backgroundImage }));
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
