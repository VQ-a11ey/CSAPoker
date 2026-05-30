package com.example;
/**
 * @author Sophia Fan
 * @date 5/27/2026
 * The Player class represents a player in the poker game.
 * It contains information about the player's name, chip count, and cards.
 */
public class Player {
    String name;
    int chips;
    Card cardOne;
    Card cardTwo;
    int bet;
    int loans;
/**
 * constructor for the Player class, initializes the player's name to an empty string, chips to 0, cards to null, bet to 0, and loans to 0
 */
    public Player() {
        name = "";
        chips = 0;
        cardOne = null;
        cardTwo = null;
        bet = 0;
        loans = 0;
    }
/**
 * returns the player's first card
 * @return the player's first card
 */
    public Card getCardOne() {
        return cardOne;
    }

    /**
     * returns the player's second card
     * @return the player's second card
     */
    public Card getCardTwo() {
        return cardTwo;
    }

    /**
     * returns the player's chip count
     * @return the player's chip count
     */
    public int getChips() {
        return chips;
    }

    /**
     * returns the player's name
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the player's current bet
     * @return the player's bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * returns the player's loan count
     * @return the player's loans
     */
    public int getLoans(){
        return loans;
    }
    /**
     * checks if the player is all in (has no chips left)
     * @return true if the player is all in, false otherwise
     */
    public boolean isAllIn() {
        return chips == 0;
    }

    /**
     * sets the player's first card
     * @param one the card to set as the player's first card
     */
    public void setCardOne(Card one) {
        cardOne = one;
    }

    /**
     * sets the player's second card
     * @param two the card to set as the player's second card
     */
    public void setCardTwo(Card two) {
        cardTwo = two;
    }

    /**
     * sets the player's name
     * @param name the name to set for the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * adds chips to the player's chip count
     * @param added the number of chips to add to the player's chip count
     */

    public void addChips(int added) {
        chips += added;
    }
    /**
     * sets the player's bet to a specific amount
     * @param bet the amount to set as the player's bet
     */
    public void setBet(int bet){
        this.bet = bet;
    }
    
    /**
     * Increments the player's loan count by 1.
     */
    public void addLoan(){
        loans++;
    }
    /**
     * places a bet for the player
     * @param toBet how much the player wants to bet
     * @return updated highest bet values and how much to add to pot
     */
    public int[] bet(int toBet) { //will return [current, addToPot] values
        if (toBet <= chips) {
            bet += toBet;
            int[] result = {bet, toBet};
            chips -= toBet;
            return result;
        } else { // all in
            bet += chips;
            int[] result = {bet, chips};
            chips = 0;
            return result;
        }
    }
}
