import java.util.*;
public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to poker!");
        System.out.print("How many people will be playing? ");
        int numPlayers = sc.nextInt();
        System.out.println();
        Game game = new Game(numPlayers);
        game.runEntireGame();
    }
}