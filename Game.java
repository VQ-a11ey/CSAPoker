package com.example;
import java.util.*;

public class Game {
    private Deck deck;
    private ArrayList<Player> players;
    private ArrayList<Player> playersReference;
    private int pot;
    private int smallBlind;
    private int bigBlind;
    private ArrayList<Card> cards;
    private int firstPlayer = 0; // first player to act in the current betting round
    private int roundCount;
    private int current;
    private int currentPlayer = 0; // for each round
    private Player lastRaiser;
    private int dealer;

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
        for (Player p: players){
            playersReference.add(p);
        }
        pot = 0;
        cards = new ArrayList<Card>();
        smallBlind = 1;
        bigBlind = 2;
        roundCount = 0;
        dealCards();
        postBlindsSetFirstPlayer();
    }

    public void resetRound(){
        players = new ArrayList<Player>(playersReference);
        dealer++;
        if (dealer >= players.size()){
            dealer = 0;
        }
        deck = new Deck();
        cards.clear();
        pot = 0;
        smallBlind = 1;
        bigBlind = 2;
        roundCount = 0;
        for (Player p: players){
            p.setBet(0);
        }
        dealCards();
        postBlindsSetFirstPlayer();
        
    }
    private int smallBlindIndex(){
        if (players.size() == 2) {
            return dealer;
        }
        return (dealer + 1) % players.size();
    }

    private int bigBlindIndex(){
        if (players.size() == 2) {
            return (dealer + 1) % players.size();
        }
        return (dealer + 2) % players.size();
    }

    private int firstPreFlopPlayer(){
        if (players.size() == 2) {
            return dealer;
        }
        return (dealer + 3) % players.size();
    }

    private int firstPostFlopPlayer(){
        return firstActivePlayerAfterDealer();
    }

    private int firstActivePlayerAfterDealer(){
        for (int i = 1; i <= playersReference.size(); i++){
            Player p = playersReference.get((dealer + i) % playersReference.size());
            int activeIndex = players.indexOf(p);
            if (activeIndex != -1 && !p.isAllIn()){
                return activeIndex;
            }
        }
        return 0;
    }
    private void postBlindsSetFirstPlayer(){
        int small = smallBlindIndex();
        int big = bigBlindIndex();

        int[] smallBet = players.get(small).bet(smallBlind);
        int[] bigBet = players.get(big).bet(bigBlind);

        addToPot(smallBet[1]);
        addToPot(bigBet[1]);

        current = bigBlind;
        firstPlayer = firstPreFlopPlayer();
        currentPlayer = firstPlayer;
        lastRaiser = players.get(firstPlayer);
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

    public int getAmountToCall(){
        Player p = players.get(currentPlayer);
        return Math.max(0, current - p.getBet());
    }

    public boolean isCallAllIn(){
        Player p = players.get(currentPlayer);
        int amountToCall = getAmountToCall();
        return amountToCall > 0 && amountToCall >= p.getChips();
    }

    private boolean noMoreBetting(){
        int playersWhoCanAct = 0;
        for (Player p : players){
            if (!p.isAllIn()){
                playersWhoCanAct++;
            }
        }

        return playersWhoCanAct <= 1;
    }

    private MoveResult finishAllInHand(){
        while (cards.size() < 5){
            cards.add(deck.chooseCard());
        }

        ArrayList<Player> winners = findWinner();
        if (winners.size() > 0){
            int winnings = pot / winners.size();
            int leftover = pot % winners.size();
            winners.get(0).addChips(leftover);
            for (Player p : winners){
                p.addChips(winnings);
            }
        }

        pot = 0;
        roundCount = 4;
        return MoveResult.HAND_ENDED;
    }

    public boolean canCheck(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() == 0;
    }

    public boolean canCall(){
        return !players.get(currentPlayer).isAllIn() && getAmountToCall() > 0;
    }

    public boolean canRaise(){
        Player p = players.get(currentPlayer);
        return !p.isAllIn() && p.getChips() > getAmountToCall();
    }

    public MoveResult fold(){
        Player p = players.get(currentPlayer);
        boolean end = (p == lastRaiser);
        players.remove(currentPlayer);
        
        if (currentPlayer < firstPlayer) {
            firstPlayer--;
            if (firstPlayer < 0) {
                firstPlayer += players.size();
            }
        }
        if (currentPlayer >= players.size()) {
            currentPlayer = 0;
        }
        if (players.size() == 1){
            roundCount = 3;
            players.get(0).addChips(pot);
            pot = 0;
            return MoveResult.HAND_ENDED;
        }
        else if (end){
            return nextRound();
        }
        else{
            currentPlayer--;
            return nextPlayer();
        }
    }

    public MoveResult raise(int amount){
        if (amount <= current || players.get(currentPlayer).isAllIn()) return MoveResult.INVALID_RAISE;
        Player p = players.get(currentPlayer);
        int oldCurrent = current;
        int[] toUpdate = p.bet(amount);
        current = Math.max(toUpdate[0], current);
        addToPot(toUpdate[1]);
        if (toUpdate[0] > oldCurrent){
            lastRaiser = p;
        }
        if (noMoreBetting()){
            return finishAllInHand();
        }
        return nextPlayer();
    }

    public MoveResult check(){
        if (players.get(currentPlayer).isAllIn()){
            return MoveResult.INVALID_CHECK;
        }
        if (current > players.get(currentPlayer).getBet()){
            return MoveResult.INVALID_CHECK;
        }
        return nextPlayer();
    }

    public MoveResult call(){
        Player p = players.get(currentPlayer);
        if (p.isAllIn()){
            return MoveResult.INVALID_CALL;
        }
        if (p.getBet() >= current){
            return MoveResult.INVALID_CALL;
        }
        int[] updateTo = p.bet(current);
        addToPot(updateTo[1]);
        if (noMoreBetting()){
            return finishAllInHand();
        }
        return nextPlayer();
    }
    
    private MoveResult nextPlayer(){
        if (noMoreBetting()){
            return finishAllInHand();
        }
        do {
            currentPlayer++;
            if (currentPlayer >= players.size()){
                currentPlayer = 0;
            }
            if (players.get(currentPlayer) == lastRaiser){
                return nextRound();
            }
        } while (players.get(currentPlayer).isAllIn());
        return MoveResult.SUCCESS;
    }

    private MoveResult nextRound(){
        currentPlayer = firstPlayer;
        roundCount++;
        current = 0;
        for (Player p: players){
            p.setBet(0);
        }
        if (roundCount == 1){
            firstPlayer = firstPostFlopPlayer();
            currentPlayer = firstPlayer;

            for (int i = 0; i < 3; i++) {
                cards.add(deck.chooseCard());
            }
        } else if (roundCount == 2) {
                cards.add(deck.chooseCard());
        } else if (roundCount == 3) {
            cards.add(deck.chooseCard());
        } else if (roundCount == 4){
            ArrayList<Player> winners = findWinner();
            if (winners.size() > 0){
                int winnings = pot / winners.size();
                int leftover = pot % winners.size();
                winners.get(0).addChips(leftover);
                for (Player p : winners){
                    p.addChips(winnings);
                }
            }
            pot = 0;
            return MoveResult.HAND_ENDED;
        } 
        lastRaiser = players.get(firstPlayer);
        return MoveResult.ROUND_ADVANCED;
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
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Player getCurrentPlayer(){
        return players.get(currentPlayer);
    }
    public ArrayList<Card> getMiddleCards(){
        return cards;
    }
    public int getCurrent(){
        return current;
    }
    public int getRoundCount(){
        return roundCount;
    }
}
