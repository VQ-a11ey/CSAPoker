package com.example;
public class Player {
    String name;
    int chips;
    Card cardOne;
    Card cardTwo;
    int bet;
    int loans;

    public Player() {
        name = "";
        chips = 0;
        cardOne = null;
        cardTwo = null;
        bet = 0;
        loans = 0;
    }

    public Card getCardOne() {
        return cardOne;
    }

    public Card getCardTwo() {
        return cardTwo;
    }

    public int getChips() {
        return chips;
    }

    public String getName() {
        return name;
    }

    public int getBet() {
        return bet;
    }

    public int getLoans(){
        return loans;
    }
    public boolean isAllIn() {
        return chips == 0;
    }

    public void setCardOne(Card one) {
        cardOne = one;
    }

    public void setCardTwo(Card two) {
        cardTwo = two;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChips(int added) {
        chips += added;
    }
    public void setBet(int bet){
        this.bet = bet;
    }
    
    public void addLoan(){
        loans++;
    }
    public int[] bet(int toBet) { //will return [current, addToPot] values
        if (toBet <= chips) {
            bet += toBet;
            int[] result = {bet, toBet};
            chips -= toBet;
            return result;
        } else { // all in
            //System.out.println(name + " is going all in!");
            bet += chips;
            int[] result = {bet, chips};
            chips = 0;
            return result;
        }
    }

    public void printPlayerCards() {
        System.out.println(cardOne.getName() + " and " + cardTwo.getName());
    }

}
