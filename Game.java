import java.util.ArrayList;
import java.util.Scanner;

public class Game{
    private Deck deck;
    private ArrayList<Player> players;
    private Scanner sc;
    private int pot;
    private ArrayList<Card> cards;
    private int smallBlind;
    private int bigBlind;
    private int startingPlayer = 2;
    public Game(int numPlayers){
        deck = new Deck(); 
        players = new ArrayList<Player>(numPlayers);
        //initialize player stuff
        for (int i = 0; i < numPlayers; i++){
            players.add(new Player());
            players.get(i).addChips(1000);
        }  
        sc = new Scanner(System.in);//?
        for (int i = 0; i < numPlayers; i++){
            System.out.print("Enter the name of player " + (i+1) + ": ");
            players.get(i).setName(sc.next());
        }
        System.out.println();
        pot = 0;
        cards = new ArrayList<Card>();
        smallBlind = 1;
        bigBlind = 2;
    }
    public void dealCards(){
        for (Player p: players){
            p.setCardOne(deck.chooseCard());
            p.setCardTwo(deck.chooseCard());
        }
    }
    public void addToPot(int i){
        pot += i;
    } 
    public int getPot(){
        return pot;
    } 
    public void resetPot(){
        pot = 0;
    } 
    public void printInfo(){
        System.out.println("There are " + pot + " chips in the pot.");
        for (Player p: players){
            System.out.println(p.getName() + " has " + p.getChips() + " chips");
        }
        if (cards.size() != 0){
            //System.out.print("The cards on the table are: ");
            for (Card c: cards){
                //System.out.print(c.getName() + "    ");
            }
            //System.out.println();
        }
        System.out.println();
    }
    public void runRound(int bet){
        int current = bet;
        ArrayList<Integer> putIn = new ArrayList<>(); // shows how much each player has put in so far to keep track of how much to call
        for (int i = 0; i < players.size(); i++){
            putIn.add(0);
        }
        if (bet != 0){
            putIn.set(0, smallBlind);
            players.get(0).call(smallBlind);
            pot += smallBlind;
            System.out.println(players.get(0).getName() + " has just paid the small blind of " + smallBlind);
            putIn.set(1, bigBlind);
            players.get(1).call(bigBlind);
            pot += bigBlind;

            System.out.println(players.get(1).getName() + " has just paid the big blind of " + bigBlind + "\n");
            for (Player p: players){
                System.out.println(p.getName()+", type anything when you are ready to see your cards. ");
                sc.next();
                p.printCards();
                System.out.println("Are you done looking at your cards? type anything to move on: ");
                sc.next();
                for (int j = 0; j < 20; j++){
                    System.out.println();
                }
            }
        }
        for (int i = 0; i < players.size();i++){
            int index = startingPlayer + i;
            if (index >= players.size()){
                index -= players.size();
            }
            Player p = players.get(index);
            printInfo();
            if (current == 0 ){
                //System.out.println(p.getName() + ", enter your move(Raise,Fold,Check,Middle,Hand): ");
                //String decision = sc.next().toLowerCase();
                String decision = "";
                while (!decision.equals("raise") && !decision.equals("fold") && !decision.equals("check") ){
                    System.out.println(p.getName()+", enter your move(Raise,Fold,Check,Middle,Hand): ");
                    decision = sc.next().toLowerCase();
                    if (decision.equals("middle")){
                        for (Card card: cards){
                            System.out.print(card.getName() + "   ");
                        }
                        System.out.println();
                        decision = "reenter decision";
                    }
                    else if (decision.equals("hand")){
                        p.printCards();
                        System.out.println("Are you done looking at your cards? type anything to move on: ");
                        sc.next();
                        for (int j = 0; j < 20; j++){
                            System.out.println();
                        }
                        decision = "reenter decision";
                        System.out.print("Re-enter decision. ");
                    }
                }
                System.out.println();
                if (decision.equals("check")){
                    continue;
                } else if (decision.equals("fold")) {
                    players.remove(index);
                    putIn.remove(index);
                    i--;
                    if (index <= startingPlayer){
                        startingPlayer --;
                        if (startingPlayer < 0){
                            startingPlayer += players.size();
                        }
                    }
                    index --;
                    if (index < 0){
                        index += players.size();
                    }
                } else if (decision.equals("raise")) {
                    int input = -1;
                    while (input <=  0){ //nextline?????
                        System.out.print("input raise amount (integer): ");
                        if (sc.hasNextInt()){
                            input = sc.nextInt();
                        }
                        else{
                            sc.next();
                        }
                    }
                    int chips = p.getChips();
                    int moreChips = input - putIn.get(index);
                    if (moreChips <= chips){
                        p.raise(moreChips);
                        current = input;
                        addToPot(moreChips);
                        putIn.set(index, input);
                    } else { // all in
                        System.out.println(p.getName() + " is going all in!");
                        p.raise(chips);
                        current = chips + putIn.get(index);
                        addToPot(chips);
                        putIn.set(index, current);
                    }
                }
            } else {
                //System.out.println(p.getName() + ", enter your move(Raise,Fold,Call,Middle,Hand): ");
                //String decision = sc.next().toLowerCase();
                String decision = "";
                while (!decision.equals("raise") && !decision.equals("fold") && !decision.equals("call")){
                    System.out.println(p.getName()+", enter your move(Raise,Fold,Call,Middle,Hand): ");
                    decision = sc.next().toLowerCase();
                    if (decision.equals("middle")){
                        for (Card card: cards){
                            System.out.print(card.getName() + "   ");
                        }
                        System.out.println();
                        decision = "go back";
                    }
                    else if (decision.equals("hand")){
                        p.printCards();
                        System.out.println("Are you done looking at your cards? type anything to move on: ");
                        sc.next();
                        for (int j = 0; j < 20; j++){
                            System.out.println();
                        }
                        decision = "reenter decision";
                        System.out.print("Re-enter decision. ");
                    }
                }
                System.out.println();
                if (decision.equals("fold")) {
                    players.remove(index);
                    putIn.remove(index);
                    i--;
                    if (index <= startingPlayer){
                        startingPlayer --;
                        if (startingPlayer < 0){
                            startingPlayer += players.size();
                        }
                    }
                    index --;
                    if (index < 0){
                        index += players.size();
                    }
                } else if (decision.equals("raise")) {
                    int input = -1;
                    while (input <= current){ //nextline?????
                        System.out.print("input raise amount (integer): ");
                        if (sc.hasNextInt()){
                            input = sc.nextInt();
                        }
                        else{
                            sc.next();
                        }
                    }
                    int chips = p.getChips();
                    int moreChips = input - putIn.get(index);
                    if (moreChips <= chips){
                        p.raise(moreChips);
                        current = input;
                        addToPot(moreChips);
                        putIn.set(index, input);
                    } else { // all in
                        System.out.println(p.getName() + " is going all in!");
                        p.raise(chips);
                        current = chips + putIn.get(index);
                        addToPot(chips);
                        putIn.set(index, current);
                    }
                } else if (decision.equals("call")){
                    int moreChips = current - putIn.get(index);
                    p.call(moreChips);
                    addToPot(moreChips);
                    putIn.set(index, current);
                }
            } 
            if (players.size() == 1){
                break;
            }
            if (i == players.size() - 1 && putIn.get(index) > putIn.get(startingPlayer)){ // go back to match raise
                i = -1;
                continue;
            }
            if (i == players.size() - 2 && bet != 0){
                continue;
            }
            int check = index + 1;
            if (check == players.size()){
                check = 0;
            }
            if (current != 0 && putIn.get(index) == putIn.get(check)){
                break;
            }
        }
        
        if (players.size() == 1){
            return;
        }
    }
    public void runEntireGame(){
        int roundCount = 0;
        while(roundCount <= 4 && players.size() >= 2){
            if (roundCount == 0){
                dealCards();
                runRound(bigBlind);
            }
            else if (roundCount == 1) {
                //reveal first 3 cards
                System.out.println("Here's the flop!");
                for (int i = 0; i < 3; i++){
                    cards.add(deck.chooseCard());
                    System.out.print(cards.get(i).getName() + "   ");
                }
                System.out.println();
                runRound(0);
            }
            else if (roundCount == 2) {
                cards.add(deck.chooseCard());
                System.out.println("Here's the river!");
                for (int i = 0; i < 4; i++){
                    System.out.print(cards.get(i).getName() + "   ");
                }
                System.out.println();
                runRound(0);
            }
            else if (roundCount == 3) {
                System.out.println("Here's the turn!");
                cards.add(deck.chooseCard());
                for (int i = 0; i < 5; i++){
                    System.out.print(cards.get(i).getName() + "   ");
                }
                System.out.println();
                runRound(0);
            }
            
            //First is after dealing cards to each player
            //Second is after the flop is revealed
            //Third is after the river
            //Fourth after the turn
            else if (roundCount == 4) {
                //Show remaining players’ cards
                System.out.println("The round is over!\nType anything when you are ready to reveal everyone's cards.");
                sc.next();
                System.out.println("\nHere are everyone's cards!");
                for (Player p: players){
                    System.out.print(p.getName() + ": ");
                    p.printCards();
                }
                System.out.println("\nThe cards in the middle were: ");
                for (Card c: cards){
                    System.out.print(c.getName() + "    ");
                }
                System.out.println();
                System.out.println();
                ArrayList<Player> winners = findWinner();
                if (winners.size() > 1){
                    for (Player p: winners){
                        p.addChips(getPot()/(winners.size()));
                        System.out.print( p + " ");
                    }
                    System.out.println(" are the winners!");
                    resetPot();
                } else{
                    if (winners.size() == 0){
                        System.out.println("findWinner is still under development.");
                        break;
                    }
                    Player winner = winners.get(0);
                    winner.addChips(getPot());
                    PlayerPoints points = new PlayerPoints(winner.getCardOne(), winner.getCardTwo(), cards);
                    int playerPoint = points.calculatePoints();
                    System.out.println(winner.getName() + " won the game with a " + handType(playerPoint) +"!");
                    resetPot();
                }
            } 
            else {
                System.out.println(players.get(0).getName() + " wins!");
                resetPot();
            }
            roundCount++;
            if (players.size() == 1){
                System.out.println("The winner is " + players.get(0).getName()+ "!");
                break;
            }
            startingPlayer ++;
            if (startingPlayer >= players.size()){
                startingPlayer = 0;
            }
        }
    }
    public ArrayList<Player> findWinner(){
        ArrayList<Player> winners = new ArrayList<>();
        int maxPoints = -1;
        for (Player p : players){
            PlayerPoints points = new PlayerPoints(p.getCardOne(), p.getCardTwo(), cards);
            int playerPoint = 0;
            playerPoint = points.calculatePoints();
            if(playerPoint > maxPoints){
                maxPoints = playerPoint;
                winners.clear();
                winners.add(p);
            } else if (playerPoint == maxPoints){
                winners.add(p);
            }
        }
        return winners;
    }
    public String handType(int points){
        if (points == 1350){
            return "Royal Flush";
        }
        if (points >= 1200){
            return "Straight Flush";
        }
        if (points >= 1050){
            return "Four of a Kind";
        }
        if (points >= 900){
            return "Full House";
        }
        if (points >= 750){
            return "Flush";
        }
        if (points >= 600){
            return "Straight";
        }
        if (points >= 450){
            return "Three of a Kind";
        }
        if (points >= 300){
            return "Two Pair";
        }
        if (points >= 150){
            return "Pair";
        }
        else{
            return "High Card";
        }
    }
}
