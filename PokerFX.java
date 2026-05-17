package com.example;
import java.net.URL;
import java.util.ArrayList;
// javafx imports are bellow
import javafx.animation.FadeTransition; // for fading
import javafx.application.Application; // output window
import javafx.geometry.Insets; // used for padding for boxes (buttons, text fields ... )
import javafx.geometry.Pos; // position the boxes
import javafx.scene.Scene; //set scene
import javafx.scene.control.Button;// for commands - call, fold, raise, etc.
import javafx.scene.control.Label; // for text / commentary
import javafx.scene.control.TextField; // scanner (get num of players, player names, etc.)
import javafx.scene.effect.DropShadow; // for visuals
import javafx.scene.image.Image; // for importing images like the dealer and the loan shark
import javafx.scene.image.ImageView; // to open image in the window
import javafx.scene.layout.*; // manage the sizing when the window is resized
import javafx.scene.media.Media; // for playing background music
import javafx.scene.media.MediaPlayer; // for playing backgroun music
import javafx.scene.paint.Color; //coloring
import javafx.scene.shape.Ellipse; // for poker round table 
import javafx.scene.shape.Rectangle; // for player rectangles on the side
import javafx.scene.text.Font; // fonts
import javafx.scene.text.FontWeight; // font size
import javafx.stage.Modality; // block other windows?? not completely sure about this one 
import javafx.stage.Stage; // setting stage
import javafx.stage.StageStyle; // stage style...
import javafx.util.Duration; //duration something appears

public class PokerFX extends Application {
    //parameters
    private Game game;
    private MediaPlayer music; 
    private Label dealerCommentary;     
    private Label pot;
    private Label communityCards;
    private Label currentBet;
    private VBox  playerBox;  
    private Label currentPlayer;
    private Button callOrCheck, fold, raise;
    private HBox  raiseBox;
    private TextField raiseField;

    @Override
    public void start(Stage stage){
        StackPane root = new StackPane();
        // Set background
         URL bgUrl = getClass().getResource("/pokerbackground.png");
        if (bgUrl != null) {
            rootPane.setStyle(
                "-fx-background-image: url('" + bgUrl.toExternalForm() + "');" + "-fx-background-size: cover;" + "-fx-background-position: center;");
        } else {
            rootPane.setStyle("-fx-background-color: #1a3a1a;");
        }
        // Set music
         URL musicUrl = getClass().getResource("/FIREpokermusic.m4a");
        if (musicUrl != null) {
            Media media = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }

        welcomeScene(stage, root);
    }
    // scene 1, welcome screen, where players enter the amount of players
    private void welcomeScene(Stage stage, StackPane root){
        // box for welcome screen label
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(340);
        box.setStyle(
            "-fx-background-color: rgba(0,0,0,0.72);" + "-fx-background-radius: 18;" + "-fx-padding: 36 40 36 40;"); //padding is where text is located in the box
        Label title = new Label("Welcome to Poker!");
        //image of dealer attached maybe here? next to the label
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f0c040; ");

        Label sub = new Label("How many players?");
        sub.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 15px;");
// enter how many players here
        TextField numField = new TextField();
        numField.setMaxWidth(140);
        numField.setStyle(
            "-fx-background-color: #2a2a2a; -fx-text-fill: white;" + "-fx-border-color: #f0c040; -fx-border-radius: 6; -fx-background-radius: 6;" + "-fx-font-size: 15px; -fx-padding: 6 10;");
     // to few players
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 12px;");
 // proceed
        Button startBtn = new Button("Start Game");
        startBtn.setOnAction(e -> {
            try {
                int n = Integer.parseInt(numField.getText().trim());
                if (n < 2) throw new Exception();
                nameEntryScreen(stage, root, n);
            } catch (Exception ex) {
                errLabel.setText("Please enter a valid number (2+)");
            }
        });
 // update the scenes , basically adds all the buttons, textfields, we just added
        box.getChildren().addAll(title, sub, numField, errLabel, startBtn);
        root.getChildren().setAll(box);
    }

    // scene 2, entering name of players given the amount of players 
    private void nameEntryScreen(Stage stage, StackPane root, int n){
        
    }        
}
