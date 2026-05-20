package com.example;
import java.util.*;

public class Game {
    private Deck deck;
    private ArrayList<Player> players;
    private ArrayList<Player> playersReference;
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
    private boolean[] inactive;

    public Game(ArrayList<String> playerNames) {
        if (playerNames.size() < 2){
            throw new IllegalArgumentException("Poker needs at least 2 players.");
        }
        deck = new Deck();
        players = new ArrayList<Player>();
        inactive = new boolean[playerNames.size()];
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
        roundCount = 0;
        dealCards();
        setFirstPlayer();
    }

    public void resetRound(){
        players.clear();
        for (int i = 0; i < playersReference.size(); i++){
            if (!inactive[i]) {
                players.add(playersReference.get(i));
            }
        }
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
        setFirstPlayer();
        
    }

    private void setFirstPlayer(){
        int small = (dealer + 1) % players.size();
        int big = (dealer + 2) % players.size();
        if (players.size() == 2){ // different with two players (heads up)
            small = dealer;
            big = (dealer + 1) % players.size();
        }

        int[] smallUpdates = players.get(small).bet(smallBlind);
        int[] bigUpdates = players.get(big).bet(bigBlind);

        addToPot(smallUpdates[1]);
        addToPot(bigUpdates[1]);

        current = bigBlind;
        firstPlayer = (dealer + 3) % players.size();
        if (players.size() == 2){
            firstPlayer = dealer;
        }
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

        /*
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
        */
        roundCount = 4;
        return MoveResult.HAND_ENDED;
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
            //players.get(0).addChips(pot);
            //pot = 0;
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
        amount += getAmountToCall();
        if (!canRaiseBy(amount)) return MoveResult.INVALID_RAISE;
        Player p = players.get(currentPlayer);
        int oldCurrent = current;
        int[] toUpdate = p.bet(amount );
        current = Math.max(toUpdate[0], current);
        addToPot(toUpdate[1]);
        if (toUpdate[0] > oldCurrent){
            lastRaiser = p;
        }
        if (noMoreBetting()){
            //return finishAllInHand();
        }
        return nextPlayer();
    }

    public MoveResult check(){
        if (!canCheck()){
            return MoveResult.INVALID_CHECK;
        }
        return nextPlayer();
    }

    public MoveResult call(){
        Player p = players.get(currentPlayer);
        if (!canCall()){
            //return MoveResult.INVALID_CALL;
        }
        int[] updateTo = p.bet(getAmountToCall());
        addToPot(updateTo[1]);
        if (noMoreBetting()){
            //return finishAllInHand();
        }
        return nextPlayer();
    }

    private MoveResult nextPlayer(){
        if (noMoreBetting()){
            //return finishAllInHand();
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
        if (noMoreBetting()){
            return finishAllInHand();
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
            /*
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
            */
            return MoveResult.HAND_ENDED;
        } 
        lastRaiser = players.get(firstPlayer);
        return MoveResult.ROUND_ADVANCED;
    }

    public int[] splitWinnings(){
        ArrayList<Player> winners = findWinner();
        int[] added = new int[winners.size()];
        if (winners.size() > 0) {
            int winnings = pot / winners.size();
            int leftover = pot % winners.size();
            int index = (dealer + 1) % playersReference.size();
            while (leftover > 0){
                int temp = players.indexOf(playersReference.get(index));
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
                winners.get(0).addChips(added[i]);
            }
        }
        return added;
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
    public ArrayList<Player> getPlayersReference(){
        return playersReference;
    }
    public void setInactive(Player p){
        inactive[playersReference.indexOf(p)] = true;
        players.remove(p);
    }
}
