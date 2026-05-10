import java.util.*;

public class Game {
    private Deck deck;
    private ArrayList<Player> players;
    private Scanner sc;
    private int pot;
    private ArrayList<Card> cards;
    private int smallBlind;
    private int bigBlind;
    private int startingPlayer = 2;

    public Game(int numPlayers) {
        deck = new Deck();
        players = new ArrayList<Player>(numPlayers);
        // initialize player stuff
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
            players.get(i).addChips(1000);
        }
        sc = new Scanner(System.in);// ?
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter the name of player " + (i + 1) + ": ");
            players.get(i).setName(sc.next());
        }
        System.out.println();
        pot = 0;
        cards = new ArrayList<Card>();
        smallBlind = 1;
        bigBlind = 2;
    }

    public void dealCards() {
        for (Player p : players) {
            p.setCardOne(deck.chooseCard());
            p.setCardTwo(deck.chooseCard());
        }
    }

    public void addToPot(int i) {
        pot += i;
    }

    public int getPot() {
        return pot;
    }

    public void resetPot() {
        pot = 0;
    }

    public void printChipsInfo() {
        System.out.println("There are " + pot + " chips in the pot.");
        for (Player p : players) {
            System.out.println(p.getName() + " has " + p.getChips() + " chips");
        }
        System.out.println();
    }

    public void printMiddleCards() {
        if (cards.size() == 0){
            System.out.println("No cards have been revealed yet.");
            return;
        }
        for (Card card : cards) {
            System.out.print(card.getName() + "   ");
        }
        System.out.println();
        System.out.println();
    }

    public void checkHand(Player p) {
        System.out.println(
                p.getName() + ", type anything when you are ready to see your cards (other players please look away).");
        sc.next();
        p.printPlayerCards();
        System.out.println("Are you done looking at your cards? type anything to move on: ");
        sc.next();
        for (int j = 0; j < 20; j++) {
            System.out.println();
        }
    }

    public int fold(int index) {
        players.remove(index);
        if (index <= startingPlayer) {
            startingPlayer--;
            if (startingPlayer < 0) {
                startingPlayer += players.size();
            }
        }
        index--;
        if (index < 0) {
            index += players.size();
        }
        return index;
    }

    public int raise(Player p, int current) { // returns what current will be set to
        int input = -1;
        while (input <= current) { // nextline?????
            System.out.print("input raise amount (integer): ");
            if (sc.hasNextInt()) {
                input = sc.nextInt();
            } else {
                sc.next();
            }
        }
        int[] toUpdate = p.bet(input);
        current = toUpdate[0];
        addToPot(toUpdate[1]);
        return current;
    }

    public String makeDecision(Player p, ArrayList<String> options) {
        String decision = "";
        while (!options.contains(decision)) {
            System.out.print(p.getName()
                    + ", enter your move ("
                    + options.get(0) + ", " + options.get(1) + ", " + options.get(2) + ", middle, or hand): ");
            decision = sc.next().toLowerCase();
            if (decision.equals("middle")) {
                printMiddleCards();
                decision = "reenter decision";
                System.out.print("Re-enter decision. ");
            } else if (decision.equals("hand")) {
                checkHand(p);
                decision = "reenter decision";
                System.out.print("Re-enter decision. ");
            }
        }
        return decision;
    }

    public void runRound(int bet) {
        int current = bet;
        ArrayList<Integer> putIn = new ArrayList<>(); // shows how much each player has put in so far to keep track of
                                                      // how much to call
        for (int i = 0; i < players.size(); i++) {
            putIn.add(0);
        }
        if (bet != 0) {
            players.get(0).bet(smallBlind);
            addToPot(smallBlind);
            System.out.println(players.get(0).getName() + " has just paid the small blind of " + smallBlind);
            players.get(1).bet(bigBlind);
            addToPot(bigBlind);
            System.out.println(players.get(1).getName() + " has just paid the big blind of " + bigBlind + "\n");

            for (Player p : players) {
                checkHand(p);
            }
        }
        for (int i = 0; i < players.size(); i++) {
            int index = startingPlayer + i;
            if (index >= players.size()) {
                index -= players.size();
            }
            Player p = players.get(index);
            printChipsInfo();
            if (current == 0) {
                String decision = makeDecision(p, new ArrayList<>(List.of("raise", "fold", "check")));
                System.out.println();
                if (decision.equals("check")) {
                    continue;
                } else if (decision.equals("fold")) {
                    i--;
                    index = fold(index);
                } else if (decision.equals("raise")) {
                    current = raise(p, current);
                }
            } else {
                // System.out.println(p.getName() + ", enter your
                // move(Raise,Fold,Call,Middle,Hand): ");
                // String decision = sc.next().toLowerCase();
                String decision = "";
                if (current == p.getBet()) {
                    decision = makeDecision(p, new ArrayList<>(List.of("raise", "fold", "check")));
                    
                } else {
                    decision = makeDecision(p, new ArrayList<>(List.of("raise", "fold", "call")));
                }
                System.out.println();
                if (decision.equals("fold")) {
                    i--;
                    index = fold(index);
                } else if (decision.equals("raise")) {
                    current = raise(p, current);
                } else if (decision.equals("call")) {
                    int[] toUpdate = p.bet(current);
                    addToPot(toUpdate[1]);
                } else if (decision.equals("check")) {
                    continue;
                }
            }
            if (players.size() == 1) {
                break;
            }
            if (i == players.size() - 1 && p.getBet() > players.get(startingPlayer).getBet()) { // go back to match raise
                i = -1;
                continue;
            }
            if (i == players.size() - 2 && bet != 0) {
                continue;
            }
            int check = index + 1;
            if (check == players.size()) {
                check = 0;
            }
            if (current != 0 && p.getBet() == players.get(check).getBet()) {
                break;
            }
        }

        if (players.size() == 1) {
            return;
        }
    }

    public void runEntireGame() {
        int roundCount = 0;
        System.out.println("At any point in the game, you can enter 'hand' to check your hand and 'middle' to check the cards in the middle.");
        System.out.println("Type anything to get the round started.");
        sc.next();
        System.out.println();
        while (roundCount <= 4 && players.size() >= 2) {
            if (roundCount == 0) {
                dealCards();
                runRound(bigBlind);
            } else if (roundCount == 1) {
                // reveal first 3 cards
                System.out.println("Here's the flop!");
                for (int i = 0; i < 3; i++) {
                    cards.add(deck.chooseCard());
                }
                printMiddleCards();
                runRound(0);
            } else if (roundCount == 2) {
                cards.add(deck.chooseCard());
                System.out.println("Here's the river!");
                printMiddleCards();
                runRound(0);
            } else if (roundCount == 3) {
                System.out.println("Here's the turn!");
                cards.add(deck.chooseCard());
                printMiddleCards();
                runRound(0);
            }

            // First is after dealing cards to each player
            // Second is after the flop is revealed
            // Third is after the river
            // Fourth after the turn
            else if (roundCount == 4) {
                // Show remaining players’ cards
                System.out.println("The round is over!\nType anything when you are ready to reveal everyone's cards.");
                sc.next();
                System.out.println("\nHere are everyone's cards!");
                for (Player p : players) {
                    System.out.print(p.getName() + ": ");
                    p.printPlayerCards();
                }
                System.out.println("\nThe cards in the middle were: ");
                printMiddleCards();
                ArrayList<Player> winners = findWinner();
                if (winners.size() > 1) {
                    int winnings = getPot() / (winners.size());
                    for (Player p : winners) {
                        p.addChips(winnings);
                        System.out.print(p.getName() + " ");
                    }
                    System.out.println("are the winners!");
                    System.out.println("They each had a " + handType(winners.get(0)) + " and each won " + winnings + " chips.");
                    resetPot();
                } else {
                    if (winners.size() == 0) {
                        System.out.println("findWinner is still under development.");
                        break;
                    }
                    Player winner = winners.get(0);
                    winner.addChips(getPot());
                    System.out.println(winner.getName() + " won the game with a " + handType(winner) + " and won " + getPot() + " chips!");
                    resetPot();
                }
            } else {
                System.out.println(players.get(0).getName() + " wins!");
                resetPot();
            }
            roundCount++;
            if (players.size() == 1) {
                System.out.println("The winner is " + players.get(0).getName() + "!");
                System.out.println(players.get(0).getName() + " won " + getPot() + " chips.");
                players.get(0).addChips(getPot());
                resetPot();
                break;
            }
            startingPlayer++;
            if (startingPlayer >= players.size()) {
                startingPlayer = 0;
            }
        }
        System.out.println("Do you want to play another round? Enter yes to continue playing. ");
        String decision = sc.next().toLowerCase();
        if (decision.equals("yes")){
            runEntireGame();
        }
    }

    public ArrayList<Player> findWinner() {
        ArrayList<Player> winners = new ArrayList<>();
        int maxPoints = -1;
        for (Player p : players) {
            PlayerPoints points = new PlayerPoints(p.getCardOne(), p.getCardTwo(), cards);
            int playerPoint = 0;
            playerPoint = points.calculatePoints();
            if (playerPoint > maxPoints) {
                maxPoints = playerPoint;
                winners.clear();
                winners.add(p);
            } else if (playerPoint == maxPoints) {
                winners.add(p);
            }
        }
        return winners;
    }

    public String handType(Player winner) {
        PlayerPoints pointSystem = new PlayerPoints(winner.getCardOne(), winner.getCardTwo(), cards);
        int points = pointSystem.calculatePoints();
        if (points == 2250) {
            return "Royal Flush";
        }
        if (points >= 2000) {
            return "Straight Flush";
        }
        if (points >= 1750) {
            return "Four of a Kind";
        }
        if (points >= 1500) {
            return "Full House";
        }
        if (points >= 1250) {
            return "Flush";
        }
        if (points >= 1000) {
            return "Straight";
        }
        if (points >= 750) {
            return "Three of a Kind";
        }
        if (points >= 500) {
            return "Two Pair";
        }
        if (points >= 250) {
            return "Pair";
        } else {
            return "High Card";
        }
    }
}
