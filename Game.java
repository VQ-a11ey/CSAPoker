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
    public void runRound(int bet){
        int current = bet;
        ArrayList<Integer> putIn = new ArrayList<>(); // shows how much each player has put in so far to keep track of how much to call
        for (int i = 0; i < players.size(); i++){
            putIn.add(0);
        }
        if (bet != 0){
            putIn.set(0, bigBlind);
            putIn.set(1, smallBlind);
        }
        for (int i = 0; i < players.size();i++){
            int index = startingPlayer + i;
            if (index >= players.size()){
                index = 0;
            }
            Player p = players.get(index);
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
                        String input = "";
                        while (!input.equals("yes") && !input.equals("no")){
                            System.out.println("Are you done looking at your cards? (yes or no): ");
                            input = sc.next().toLowerCase();
                        }
                        for (int j = 0; j < 20; j++){
                            System.out.println();
                        }
                        decision = "reenter decision";
                        System.out.print("Re-enter decision. ");
                    }
                }
                if (decision.equals("check")){
                    break;
                } else if (decision.equals("fold")) {
                    players.remove(index);
                    putIn.remove(index);
                    i--;
                } else if (decision.equals("raise")) {
                    int input = -1;
                    while (!sc.hasNextInt() || input <=  0){ //nextline?????
                        System.out.println("input raise amount (integer) : ");
                        input = sc.nextInt();
                    }
                    int chips = p.getChips();
                    if (input <= chips){
                        p.raise(input);
                        current = input;
                        addToPot(input);
                    } else { // all in
                        System.out.println(p.getName() + " is going all in!");
                        p.raise(chips);
                        current = chips;
                        addToPot(chips);
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
                        String input = "";
                        while (!input.equals("yes") && !input.equals("no")){
                            System.out.println("Are you done looking at your cards? (yes or no): ");
                            input = sc.next().toLowerCase();
                        }
                        for (int j = 0; j < 20; j++){
                            System.out.println();
                        }
                        decision = "reenter decision";
                        System.out.print("Re-enter decision. ");
                    }
                }
                if (decision.equals("fold")) {
                    players.remove(index);
                    putIn.remove(index);
                    i--;
                } else if (decision.equals("raise")) {
                    int input = -1;
                    while (!sc.hasNextInt() || input <=  0 || input <= current){
                        if (input <= current) {System.out.print("raise is too low, ");}
                        System.out.println("input raise amount (integer) : ");
                        input = sc.nextInt();
                    }
                    int chips = p.getChips();
                    if (input <= chips){
                        p.raise(input);
                        current = input;
                        addToPot(input);
                    } else { // all in
                        System.out.println(p.getName() + " is going all in!");
                        p.raise(chips);
                        current = chips;
                        addToPot(chips);
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
            if (i == players.size() - 1 && putIn.get(i) > putIn.get(0)){ // go back to match raise
                i = 0;
            }
        }
        startingPlayer ++;
        if (startingPlayer >= players.size()){
            startingPlayer = 0;
        }
        if (players.size() == 1){
            return;
        }
    }
    public void runEntireGame(){
        int roundCount = 0;
        while(roundCount < 4 && players.size() >= 2){
            if (roundCount == 0){
                dealCards();
                runRound(bigBlind);
            }
            else if (roundCount == 1) {
                //reveal first 3 cards
                for (int i = 0; i < 3; i++){
                    cards.add(deck.chooseCard());
                    System.out.print(cards.get(i).getName() + "   ");
                }
                System.out.println();
                runRound(0);
            }
            else if (roundCount == 2) {
                cards.add(deck.chooseCard());
                for (int i = 0; i < 4; i++){
                    System.out.print(cards.get(i).getName() + "   ");
                }
                System.out.println();
                runRound(0);
            }
            else if (roundCount == 3) {
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
                for (Player p: players){
                    System.out.print(p.getName() + ": ");
                    p.printCards();
                }
                if (findWinner().size() > 1){
                    //For each player.setchips(getPot()/findwinner.size())
                } else{
                    findWinner().get(0).addChips(getPot());
                    resetPot();
                }
            } 
            else {
                System.out.println(players.get(0).getName() + " wins!");
            }resetPot();
            roundCount++;
            if (players.size() == 1){
                System.out.println("Our winner is " + players.get(0).getName()+ "!");
                break;
            }
        }
    }
    public ArrayList<Player> findWinner(){
        ArrayList<Player> winners = new ArrayList<>();
        // find winners
        return winners;
    }
}