package com.example;
import java.util.*;
/**
 * @authors Anna Chen, Sophia Fan, Vicky Qin
 * @date 5.27.2026
 * The Game class represents a game of poker. 
 * it inlcudes the deck, players, pot, blinds, cards on the table, and the flow of the game. 
 * it has methods for resetting the round, dealing cards, handling player actions (fold, raise, check, call), determining the next player and next round, finding the winner(s), and calculating winnings. The class also keeps track of the current state of the game, including the current player, current bet, and round count.
 */
public class Game {
    private Deck deck;
    private ArrayList<Player> players;
    private ArrayList<Player> playersReference;
    private ArrayList<Player> overallPlayers;
    private int pot;
    private int smallBlind = 1;
    private int bigBlind = 2;
    private ArrayList<Card> cards;
    private int firstPlayer = 0; // first player to act in the current betting round
    private int roundCount;
    private int current;
    private int currentPlayer = 0; // for each round
    private Player lastRaiser;
    private int dealer;

    /**
     * Constructor for Game class. Initializes the game with a list of player names, creates a deck, and deals cards to each player.
     * @param playerNames
     */
    public Game(ArrayList<String> playerNames) {
        if (playerNames.size() < 2){
            throw new IllegalArgumentException("Poker needs at least 2 players.");
        }
        deck = new Deck();
        players = new ArrayList<Player>();
        // initialize player stuff
        for (int i = 0; i < playerNames.size(); i++) {
            Player p = new Player();
            p.setName(playerNames.get(i));
            p.addChips(1000);
            players.add(p);
        }
        playersReference = new ArrayList<>();
        overallPlayers = new ArrayList<Player>();
        for (Player p: players){
            playersReference.add(p);
            overallPlayers.add(p);
        }
        pot = 0;
        cards = new ArrayList<Card>();
        roundCount = 0;
        dealCards();
    }

    /**
     * Resets the game for a new round. Increments the dealer, creates a new deck, clears the middle cards and pot, resets the round count, and deals new cards to each player. Also resets each player's bet to 0.
     */
    public void resetRound(){
        players = new ArrayList<>(playersReference);
        dealer++;
        if (dealer >= players.size()){
            dealer = 0;
        }
        deck = new Deck();
        cards.clear();
        pot = 0;
        roundCount = 0;
        for (Player p: players){
            p.setBet(0);
        }
        dealCards();
    }
    public int[] getBlindIndices(){
        int small = (dealer + 1) % players.size();
        int big = (dealer + 2) % players.size();
        if (players.size() == 2){ // different with two players (heads up)
            small = dealer;
            big = (dealer + 1) % players.size();
        }
        return new int[]{small, big};
    }
    public void setFirstPlayer(){
        current = bigBlind;
        firstPlayer = (dealer + 3) % players.size();
        if (players.size() == 2){
            firstPlayer = dealer;
        }
        currentPlayer = firstPlayer;
        lastRaiser = players.get(firstPlayer);
    }
    private void dealCards() {
        for (Player p : players) {
            p.setCardOne(deck.chooseCard());
            p.setCardTwo(deck.chooseCard());
        }
    }

    public int getSmallBlind(){
        return smallBlind;
    }
    public int getBigBlind(){
        return bigBlind;   
    }
    public void addToPot(int i) {
        pot += i;
    }

    public int getPot() {
        return pot;
    }

    public int getAmountToCall(){
        Player p = players.get(currentPlayer);
        return Math.max(0, current - p.getBet());
    }

    public boolean canCheck(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() == 0;
    }

    public boolean canCall(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() > 0;
    }

    public boolean canRaiseBy(int amount){
        Player p = players.get(currentPlayer);
        return !p.isAllIn() && amount <= p.getChips();
    }

    public void fold(){
        Player p = players.get((currentPlayer + 1) % players.size());
        boolean end = (p == lastRaiser);
        
        players.remove(currentPlayer);
        
        if (currentPlayer < firstPlayer) {
            firstPlayer--;
            if (firstPlayer < 0) {
                firstPlayer += players.size();
            }
        }
        else if (currentPlayer == firstPlayer){
            if (firstPlayer >= players.size()){
                firstPlayer = 0;
            }
        }
        if (currentPlayer >= players.size()) {
            currentPlayer = 0;
        }
        if (players.size() == 1){
            roundCount = 4;
            return;
        }
        else if (end){
            nextRound();
        }
        else{
            currentPlayer--;
            nextPlayer();
        }
    }

    public void raise(int amount){
        amount += getAmountToCall();
        if (!canRaiseBy(amount)) return;
        Player p = players.get(currentPlayer);
        int oldCurrent = current;
        int[] toUpdate = p.bet(amount );
        current = Math.max(toUpdate[0], current);
        addToPot(toUpdate[1]);
        if (toUpdate[0] > oldCurrent){
            lastRaiser = p;
        }
        nextPlayer();
    }

    public void check(){
        if (!canCheck()){
            return;
        }
        nextPlayer();
    }

    public void call(){
        Player p = players.get(currentPlayer);
        int[] updateTo = p.bet(getAmountToCall());
        addToPot(updateTo[1]);
        nextPlayer();
    }

    private void nextPlayer(){
        do {
            currentPlayer++;
            if (currentPlayer >= players.size()){
                currentPlayer = 0;
            }
            if (players.get(currentPlayer) == lastRaiser){
                nextRound();
                return;
            }
            if (!players.contains(lastRaiser)){
                int i = overallPlayers.indexOf(lastRaiser);
                while (!players.contains(overallPlayers.get(i))){
                    i++;
                    if (i >= overallPlayers.size()){
                        i = 0;
                    }
                }
                lastRaiser = overallPlayers.get(i);

            }
        } while (players.get(currentPlayer).isAllIn());
    }

    private void nextRound(){
        currentPlayer = firstPlayer;
        roundCount++;
        current = 0;
        for (Player p: players){
            p.setBet(0);
        }
        // check if hand is finished
        int playersWhoCanAct = 0;
        for (Player p : players){
            if (!p.isAllIn()){
                playersWhoCanAct++;
            }
        }
        if (playersWhoCanAct <= 1){
            roundCount = 4;
            return;
        }
        if (roundCount >= 1 && roundCount <= 3){
            firstPlayer = 0;
            for (int i = 1; i <= playersReference.size(); i++){
                Player p = playersReference.get((dealer + i) % playersReference.size());
                int activeIndex = players.indexOf(p);
                if (activeIndex != -1 && !p.isAllIn()){
                    firstPlayer = activeIndex;
                    break;
                }
            }
            currentPlayer = firstPlayer;
        }
        if (roundCount == 1){
            for (int i = 0; i < 3; i++) {
                cards.add(deck.chooseCard());
            }
        } else if (roundCount == 2) {
                cards.add(deck.chooseCard());
        } else if (roundCount == 3) {
            cards.add(deck.chooseCard());
        } else if (roundCount == 4){
            return;
        } 
        lastRaiser = players.get(firstPlayer);
        return;
    }
/**
 * if multiple players win, the winnings (pot chips) are split up as evenly as possible 
 * @return 
 */
    public int[] splitWinnings(){
        ArrayList<Player> winners = findWinner();
        int[] added = new int[winners.size()];
        if (winners.size() > 0) {
            int winnings = pot / winners.size();
            int leftover = pot % winners.size();
            int index = (dealer + 1) % playersReference.size();
            while (leftover > 0){
                int temp = winners.indexOf(playersReference.get(index));
                if (temp != -1){
                    added[temp] ++;
                    leftover--;
                    index = (index + 1) % playersReference.size();
                }
            }
            for (int i = 0; i < winners.size(); i++){
                added[i] += winnings;
            }
            for (int i = 0; i < winners.size(); i++){
                winners.get(i).addChips(added[i]);
            }
        }
        return added;
    }
/**
 * finds the winner(s) of the hand by calculating the points of each player's hand and comparing them. If there are multiple players with the same highest points, they are all considered winners.
 * @return an ArrayList of the winning player(s)
 */
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
        if (points == 8300) {
            return "Royal Flush";
        }
        if (points >= 8050) {
            return "Straight Flush";
        }
        if (points >= 7800) {
            return "Four of a Kind";
        }
        if (points >= 4400) {
            return "Full House";
        }
        if (points >= 4150) {
            return "Flush";
        }
        if (points >= 3900) {
            return "Straight";
        }
        if (points >= 3650) {
            return "Three of a Kind";
        }
        if (points >= 250) {
            return "Two Pair";
        }
        if (points >= 15) {
            return "Pair";
        } else {
            return "High Card";
        }
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Player getCurrentPlayer(){
        return players.get(currentPlayer);
    }
    public int getCurrentIndex(){
        return currentPlayer;
    }
    public ArrayList<Card> getMiddleCards(){
        return cards;
    }
    public Deck getDeck(){
        return deck;
    }
    public int getCurrent(){
        return current;
    }
    public int getRoundCount(){
        return roundCount;
    }
    public ArrayList<Player> getOverallPlayers(){
        return overallPlayers;
    }
    public ArrayList<Player> getPlayersReference(){
        return playersReference;
    }
    public void setCurrentPlayer(int c){
        currentPlayer = c;
    }
    public void setInactive(Player p){
        int index = playersReference.indexOf(p);
        playersReference.remove(p);
        if (index < dealer){
            dealer--;
            if (dealer < 0){
                dealer += playersReference.size();
            }
        }
        players.remove(p);
    }
}
