package com.example;
import java.util.ArrayList;

/**
 * @author Anna Chen 
 * @date 5/27/2026
 * PlayerPoints is used to calculate the points in a player's hand and the middle cards 
 * for determining the winner of the round
 */

public class PlayerPoints extends Player{
    private int points;
    private ArrayList<Card> hand;
    private int[] counts; 
    private boolean hasRoyalFlush;
    private boolean hasStraightFlush;
    private boolean hasFourOfAKind;
    private boolean hasFullHouse;
    private boolean hasFlush;
    private boolean hasStraight;
    private boolean hasThreeOfAKind;
    private boolean hasTwoPair;
    private boolean hasPair;

    /**
     * constructor for player points, takes in the two cards from the players and the 
     * cards in the middle and puts it in an arrayList of cards used to calculate player points
     * @param one
     * @param two
     * @param cards
     */

    public PlayerPoints(Card one, Card two , ArrayList<Card> cards){
        points = 0;
        hand = new ArrayList<Card>();
        hand.add(one);
        hand.add(two);
        for (Card c: cards){
            hand.add(c);
        }
        counts = new int[15];
        for (Card c : hand){
            counts[c.getRank()]++;
        }
        hasRoyalFlush = false;
        hasStraightFlush = false;
        hasFourOfAKind = false;
        hasFullHouse = false;
        hasFlush = false;
        hasStraight = false;
        hasThreeOfAKind = false;
        hasTwoPair = false;
        hasPair = false;
    }
/**
 * @return hand of cards with player's two cards and middle cards
 */

    public ArrayList<Card> getHand(){
        return hand;
    }
/**
 * @param points sets the points of player to the parameter points
 * sets point of player to parameter points
 */
    public void setPoints(int points){
        this.points = points;
    }
/**
 * get points of player
 * @return ponts of player
 */
    public int getPoints(){
        return points;
    }
/**
 * checks if player has a pair and returns the rank of pair, -1 if no pair 
 * sets hasPair to true for later methods to use for calculating points
 * @return rank of pair if there is a pair, -1 if there is no pair
 */
    public int hasPair(){
        int highestPair = -1;
        for (int i = 2; i <= 14; i++){
            if (counts[i] >= 2){
                if (i > highestPair){
                    highestPair = i;
                }
            }
        }
        hasPair = (highestPair != -1);
        return highestPair;
    }
/**
 * checks if player has a twoPair pattern
 * sets hasTwoPair to true for calculating points later
 * @return an array of the ranks of the two pairs, with the first element
 * being the higher pair, and the second element being the lower pair, if there is no pair, it returns an array of -1, -1
 */
    public int[] hasTwoPair(){
        int[] twoPair = {-1, -1}; // 1 is highest pair, 2 is second highest pair
        for (int i = 2; i <= 14; i++){
            if (counts[i] >= 2){
                if (i > twoPair[0]){
                    twoPair[1] = twoPair[0];
                    twoPair[0] = i;
                } else if (i > twoPair[1]){
                    twoPair[1] = i;
                }
            }
        }
        hasTwoPair = (twoPair[1] != -1);
        return twoPair;
    }
/**
 * checks if player has three of a kind pattern
 * sets hasThreeOfAKind to true for calculating points later
 * @return rank of three of a kind if there is one, -1 if there is no three of a kind
 */
    public int threeOfAKind(){
        int three = -1;
        for (int i = 2; i <= 14; i++){
            if (counts[i] >= 3){
                if (i > three){
                    three = i;
                }
            }
        }
        hasThreeOfAKind = (three != -1);
        return three;
    }
/**
 * checks if player has four of a kind pattern
 * sets hasFourOfAKind to true for calculating points later
 * @return rank of four of a kind if there is one, -1 if there is no four of a kind
 */
    public int hasFourOfAKind(){
        int four = -1;
        for (int i = 2; i <= 14; i++){
            if (counts[i] >= 4){
                four = i;
            }
        }
        hasFourOfAKind = (four != -1);
        return four;
    }
/**
 * checks if player has straight pattern 
 * sets hasStraight to true for calculating points later
 * @return an arrayList of the ranks of the straights in the player's hand, if there are no straights, it returns an empty arrayList
 * the returned arrayList is used to calculate points since the last element is the highest straight
 * and it is also used in hasStraightFlush to check if any of the straights is a flush 
 */
    public ArrayList<Integer> hasStraight(){
       ArrayList<Integer> straights = new ArrayList<Integer>();
       int[] ranks = new int[hand.size()];
       for (int i = 0; i < hand.size(); i++){
           ranks[i] = hand.get(i).getRank();
       }
       for (int i = 0; i < ranks.length - 1; i++){
           for (int j = i + 1; j < ranks.length; j++){
               if (ranks[i] > ranks[j]){
                    int temp = ranks[i];
                    ranks[i] = ranks[j];
                    ranks[j] = temp;
               }
           }
       }
       int straight = 1;
       for (int i = 0; i < ranks.length - 1; i++){
           if (ranks[i]== ranks[i+1]){
               continue;
           } else if (ranks[i]+1 == ranks[i+ 1]){
               straight++;
               if (straight >= 5){
                   straights.add(ranks[i+1]);
               }
           } else {
               straight = 1;
           }
       }
       if (straights.size() > 0){
           hasStraight = true;
       }
       return straights;
    }
/**
 * sets hasFlush to true for calculating points later
 * @param rayy , an array list of cards to check for flush
 * @return rank of the highest card in the flush, if there is no flush, it returns -1
 */
    public int hasFlush(ArrayList<Card> rayy){
        int[] ar = new int[4];
        int highest = -1;
        for (Card c: rayy){
            if (c.getSuit().equals("Hearts")){
                ar[0]++;
            }else if (c.getSuit().equals("Spades")){
                ar[1]++;
            }else if (c.getSuit().equals("Diamonds")){
                ar[2]++;
            }else{
                ar[3]++;
            }
        }
        for (Card c: hand){
            if (ar[0] >= 5 && c.getSuit().equals("Hearts") && c.getRank() > highest){
                highest = c.getRank();
            } else if (ar[1] >= 5 && c.getSuit().equals("Spades") && c.getRank() > highest){
                highest = c.getRank();
            } else if (ar[2] >= 5 && c.getSuit().equals("Diamonds") && c.getRank() > highest){
                highest = c.getRank();
            } else if (ar[3] >= 5 && c.getSuit().equals("Clubs") && c.getRank() > highest){
                highest = c.getRank();
            }
        }
        hasFlush = (ar[0] >= 5 || ar[1] >= 5 || ar[2] >= 5 || ar[3] >= 5);
        return highest;
    }
/**
 * checks if player has a full house pattern
 * sets hasFullHouse to true for calculating points later
 * @return a number that is the rank of the three of a kind times 15 squared plus the rank of the pair times 15, if there is no full house, it returns -1
 * the returned number is used to calculate points 
 */
    public int hasFullHouse(){
        int three = -1;
        int pair = -1;
        for (int i = 2; i <= 14; i++){
            if (counts[i] == 3 && i > three){
                three = i;
            }
        }
        for (int i = 2; i <= 14; i++){
            if (counts[i] == 2 && i != three && i > pair){
                pair = i;
            }
        }
        if (three != -1 && pair != -1) {
            hasFullHouse = true;
            return three * 15 * 15+ pair * 15;
        }
        hasFullHouse = false;
        return -1;
    }
/**
 * checks if player has a straight flush pattern
 * sets hasStraightFlush to true for calculating points later
 * @return rank of the highest card in the straight flush, if there is no straight flush, it returns -1
 */
    public int hasStraightFlush(){
        
      int highestss = -1;
        int[] ranks = new int[hand.size()];
       for (int i = 0; i < hand.size(); i++){
           ranks[i] = hand.get(i).getRank();
       }
       for (int i = 0; i < ranks.length - 1; i++){
           for (int j = i + 1; j < ranks.length; j++){
               if (ranks[i] > ranks[j]){
                    int temp = ranks[i];
                    ranks[i] = ranks[j];
                    ranks[j] = temp;
               }
           }
       }
       int straight = 1;
       for (int i = 0; i < ranks.length - 1; i++){
           if (ranks[i]== ranks[i+1]){
               continue;
           } else if (ranks[i]+1 == ranks[i+ 1]){
               straight++;
               if (straight >= 5){
                    String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
                    for (String suit:suits){
                       int count = 0;
                       for (int rank = ranks[i+1]; rank >= ranks[i-3]; rank--){
                           for (Card c: hand){
                               if (c.getRank() == rank && c.getSuit().equals(suit)){
                                   count++;
                               }
                           }
                       }
                       if (count == 5){
                           hasStraightFlush = true;
                           highestss= ranks[i+1];
                    }
                   }
               }
           } else {
               straight = 1;
           }
       }
        return highestss;
    }
/**
 * checks if player has a royal flush pattern
 * sets hasRoyalFlush to true for calculating points later
 * @return true if player has a royal flush, false if player does not have a royal flush
 */
    public boolean hasRoyalFlush(){
        String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
        for (String suit:suits){
            boolean ten = false;
            boolean jack = false;
            boolean queen = false;
            boolean king = false;
            boolean ace = false;
            for (Card c:hand){
                if (c.getSuit().equals(suit)){
                    if (c.getRank() == 10) {
                        ten = true;
                    }
                    if (c.getRank() == 11) {
                        jack = true;
                    }
                    if (c.getRank() == 12) {
                        queen = true;
                    }
                    if (c.getRank() == 13) {
                        king = true;
                    }
                    if (c.getRank() == 14) {
                        ace = true;
                    }
                }
            }
            if (ten && jack && queen && king && ace){
                hasRoyalFlush = true;
                return true;
            }
        }
        return false;
        
    }
/**
 * finds the rank of the highest card in the player's hand for calculating points in case of tie breakers
 * @return rank of the highest card in the player's hand, if there are no cards, it returns 0
 */
    public int highestCardRank(){ 
        int highest = 0;
        for (Card c: hand){
            if (c.getRank() > highest){
                highest = c.getRank();
            }
        }
        return highest;
    }
/**
 * calculates the points of the player's hand based on the patterns in the hand and the ranks of the cards in the hand
 * @return calculated player point based on the ir hand 
 */
    public int calculatePoints(){
        hasRoyalFlush();
        hasStraightFlush();
        hasFourOfAKind();
        hasFullHouse();
        hasFlush(hand);
        hasStraight();
        threeOfAKind();
        hasTwoPair();
        hasPair();

        if (hasRoyalFlush){
            return 8300;
        }
        if (hasStraightFlush){
            return 8050 + hasStraightFlush() * 15 + highestCardRank();
        }
        if (hasFourOfAKind){
            return 7800 + hasFourOfAKind() * 15 + highestCardRank();
        }
        if (hasFullHouse){
            return 4400 + hasFullHouse() + highestCardRank();
        }
        if (hasFlush){
            return 4150 + hasFlush(hand) * 15 + highestCardRank();
        }
        if (hasStraight){
            return 3900 + hasStraight().get(hasStraight().size() - 1) * 15 + highestCardRank();
        }
        if (hasThreeOfAKind){
            return 3650 + threeOfAKind() * 15 + highestCardRank();
        }
        if (hasTwoPair){
            return 250 + hasTwoPair()[0] * 15 * 15 + hasTwoPair()[1] * 15 + highestCardRank();
        }
        if (hasPair){
            return 15 + hasPair() * 15 + highestCardRank();
        }
        return highestCardRank(); // highest is 14 
    }

}
