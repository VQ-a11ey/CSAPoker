package com.example;
import java.util.*;
/**
 * @authors Anna Chen, Sophia Fan, Vicky Qin
 * @date 5/27/2026
 * The Game class implements the logic for a game of poker. 
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
     * @param playerNames the list of player names
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

    /**
     * Returns the indices of the small blind and big blind players based on the current dealer. The small blind is the player immediately to the left of the dealer, and the big blind is the player two positions to the left of the dealer. In a game with two players, the small blind is the dealer and the big blind is the other player.
     * @return int[] containing the indices of the small blind and big blind players
     */
    public int[] getBlindIndices(){
        int small = (dealer + 1) % players.size();
        int big = (dealer + 2) % players.size();
        if (players.size() == 2){ // different with two players (heads up)
            small = dealer;
            big = (dealer + 1) % players.size();
        }
        return new int[]{small, big};
    }

    /**
     * Sets the first player to act in the current betting round. It is the first to the left of the big blind, or in the case of 2 players, it is the dealer. Also sets the current player and last raiser to the first player.
     */
    public void setFirstPlayer(){
        current = bigBlind;
        firstPlayer = (dealer + 3) % players.size();
        if (players.size() == 2){
            firstPlayer = dealer;
        }
        currentPlayer = firstPlayer;
        lastRaiser = players.get(firstPlayer);
    }

    /**
     * Deals two cards to each player from the deck. 
     */
    private void dealCards() {
        for (Player p : players) {
            p.setCardOne(deck.chooseCard());
            p.setCardTwo(deck.chooseCard());
        }
    }

    /**
     * Returns the index of the small blind player.
     * @return the small blind
     */
    public int getSmallBlind(){
        return smallBlind;
    }
    /**
     * Returns the value of the big blind.
     * @return the big blind
     */
    public int getBigBlind(){
        return bigBlind;   
    }

    /**
     * Adds the specified amount to the pot.
     * @param i the amount to add to the pot
     */
    public void addToPot(int i) {
        pot += i;
    }

    /**
     * Returns the amount of chips in the pot.
     * @return the amount of chips in the pot
     */
    public int getPot() {
        return pot;
    }

    /**
     * Calculates the amount a player needs to call in order to match the current bet. It takes into account the player's current bet and the current highest bet in the round. If the player's current bet is less than the current highest bet, it returns the difference; otherwise, it returns 0.
     * @return int representing the amount to call
     */
    public int getAmountToCall(){
        Player p = players.get(currentPlayer);
        return Math.max(0, current - p.getBet());
    }

    /**
     * Determines if the current player can check (not all-in and there is no bet to match)
     * @return boolean indicating if the player can check
     */
    public boolean canCheck(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() == 0;
    }

    /**
     * Determines if the current player can call (not all-in and there is a bet to match)
     * @return boolean indicating if the player can call
     */
    public boolean canCall(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() > 0;
    }

    /**
     * Determines if the current player can raise by a specified amount. A player can raise if they are not all-in and the amount they want to raise by is less than or equal to their remaining chips.
     * @param amount the amount the player wants to raise by
     * @return boolean indicating if the player can raise by the specified amount
     */
    public boolean canRaiseBy(int amount){
        Player p = players.get(currentPlayer);
        return !p.isAllIn() && amount <= p.getChips();
    }

    /**
     * Handles the fold action for the current player. The player is removed from the game, and if the folded player is the last raiser, the next round is started. If there is only one player left after folding, the round count is set to 4 to show that the hand is finished. Otherwise, it moves on to the next player.
     */
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

    /**
     * Handles the raise action for the current player. The player raises by a specified amount, which is added to the current bet. If the the player has enough chips, the player's bet is updated, and the pot is increased by the amount of the raise. If the raise increases the current bet, the last raiser is updated to the current player. Finally, it moves on to the next player.
     * @param amount the amount the player wants to raise by
     */
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

    /**
     * Handles the check action for the current player. If the player can check, it moves on to the next player. Otherwise, it does nothing.
     */
    public void check(){
        if (!canCheck()){
            return;
        }
        nextPlayer();
    }

    /**
     * Handles the call action for the current player. If the player can call, it updates the player's bet to match the current bet, adds the amount called to the pot, and moves on to the next player. Otherwise, it does nothing.
     */
    public void call(){
        Player p = players.get(currentPlayer);
        int[] updateTo = p.bet(getAmountToCall());
        addToPot(updateTo[1]);
        nextPlayer();
    }

    /**
     * Moves to the next player in the game.
     */
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

    /**
     * Moves on to the next round of betting. It resets the current player to the first player, increases the round count, and resets each player's bet to 0. If the hand is finished (only one player who can act), it sets the round count to 4. If the round count is between 1 and 3, it determines the first player to act based on the position of the dealer and whether players are all-in. It then adds the appropriate number of cards to the middle based on the round count (3 cards for the flop, 1 card for the turn, and 1 card for the river). Finally, it updates the last raiser to be the first player.
     */
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
 * If multiple players win, the winnings (pot chips) are split up as evenly as possible 
 * @return an int[] representing the amount of chips each winning player receives from the pot. The order corresponds to the order of the winning players returned by findWinner().
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
 * Finds the winner(s) of the hand by calculating the points of each player's hand and comparing them. If there are multiple players with the same highest points, they are all considered winners.
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

    /**
     * Determines the type of hand the winning player has based on their points from PlayerPoints
     * @param winner the winning player whose hand type is being determined
     * @return the player's best hand type as a String (e.g. "Full House")
     */
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
    /**
     * Returns the list of players currently active in the round
     * @return the list of players currently active in the round
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
    /**
     * Returns the current player whose turn it is to act.
     * @return current player in the game
     */
    public Player getCurrentPlayer(){
        return players.get(currentPlayer);
    }
    /**
     * Returns the index of the current player in the players list.
     * @return index of the current player in the players list
     */
    public int getCurrentIndex(){
        return currentPlayer;
    }
    /**
     * Returns the list of cards currently on the table (the middle cards).
     * @return middle cards
     */
    public ArrayList<Card> getMiddleCards(){
        return cards;
    }
    /**
     * Returns the deck of cards being used in the game.
     * @return deck being used in the game
     */
    public Deck getDeck(){
        return deck;
    }
    /**
     * Returns the highest bet in the current round
     * @return current bet in the round
     */
    public int getCurrent(){
        return current;
    }
    /**
     * Returns the current round count (0 for pre-flop, 1 for flop, ... 4 for hand finished).
     * @return the round count
     */
    public int getRoundCount(){
        return roundCount;
    }
    /**
     * Returns the list of all players who started the game, including those who dropped out due to insufficient money.
     * @return all players who started the game
     */
    public ArrayList<Player> getOverallPlayers(){
        return overallPlayers;
    }
    /**
     * Returns the list of all players except those who dropped out due to insufficient money. Includes players who folded in the current round.
     * @return all players who started in the current round
     */
    public ArrayList<Player> getPlayersReference(){
        return playersReference;
    }
    /**
     * sets the current player index to the specified value. 
     * @param c what the current player index should be set to
     */
    public void setCurrentPlayer(int c){
        currentPlayer = c;
    }
    /**
     * Removes a player from the game and updates the dealer index if necessary. This is used when a player denies a loan. Updates dealer index and player lists.
     * @param p the player to remove
     */
    public void setInactive(Player p){
        int index = playersReference.indexOf(p);
        playersReference.remove(p);
        if (index <= dealer){
            dealer--;
            if (dealer < 0){
                dealer += playersReference.size();
            }
        }
        players.remove(p);
    }
}
