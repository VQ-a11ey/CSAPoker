package com.example;
import java.util.ArrayList;

/**
 * @author Anna Chen 
 * @date 5/27/2026
 * PlayerPoints is used to calculate the points in a player's hand and the middle cards 
 * for determining the winner of the round
 */

public class Points extends Player{
    private int points;
    private ArrayList<Card> hand;
    private int[] counts; 
    private boolean hasStraightFlush;
    private boolean hasFourOfAKind;
    private boolean hasFullHouse;
    private boolean hasFlush;
    private boolean hasStraight;
    private boolean hasThreeOfAKind;
    private boolean hasTwoPair;
    private boolean hasPair;
    private int[] sixThings;
    private ArrayList<Integer> pattern;

    /**
     * constructor for player points, takes in the two cards from the players and the 
     * cards in the middle and puts it in an arrayList of cards used to calculate player points
     * sets boolean has<pattern> to false for all patterns for calculating points later
     * makes empty arraylist pattern
     * and makes a sixthings array to store the integer ranks of determinant of the ranks for the hand for comparison
     * @param one
     * @param two
     * @param cards
     */

    public Points(Card one, Card two , ArrayList<Card> cards){
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
        hasStraightFlush = false;
        hasFourOfAKind = false;
        hasFullHouse = false;
        hasFlush = false;
        hasStraight = false;
        hasThreeOfAKind = false;
        hasTwoPair = false;
        hasPair = false;
        sixThings = new int[6];
        pattern = new ArrayList<Integer>();
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
 * adds pair rank to pattern for calculating points in case of tie breakers if has pair 
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
        if (hasPair) {
            pattern.add(highestPair);
        }
        return highestPair;
    }
/**
 * checks if player has a twoPair pattern
 * sets hasTwoPair to true for calculating points later
 * adds the ranks of the two pairs to pattern for calculating points if has two pair
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
        if (hasTwoPair) {
            pattern.add(twoPair[0]);
            pattern.add(twoPair[1]);
        }
        return twoPair;
    }
/**
 * checks if player has three of a kind pattern
 * sets hasThreeOfAKind to true for calculating points later
 * adds the rank of the three of a kind to pattern for calculating points if has three of a kind
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
        if (hasThreeOfAKind) {
        pattern.add(three);
        }
        return three;
    }
/**
 * checks if player has four of a kind pattern
 * sets hasFourOfAKind to true for calculating points later
 * adds the rank of the four of a kind to pattern for calculating points if has four of a kind
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
        if (hasFourOfAKind) {
            pattern.add(four);
        }
        return four;
    }
/**
 * checks if player has straight pattern 
 * sets hasStraight to true for calculating points later
 * adds the ranks of the straight to pattern for calculating points if has straight
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
       int highest = -1; 
       for (int i = 0; i < ranks.length - 1; i++){
           if (ranks[i]== ranks[i+1]){
               continue;
           } else if (ranks[i]+1 == ranks[i+ 1]){
               straight++;
               if (straight >= 5 && ranks[i+1] > highest){
                   highest = ranks[i+1];
                   straights.clear();
                   straights.add(ranks[i+1]);
               }
           } else {
               straight = 1;
           }
       }
       if (straights.size() > 0){
           hasStraight = true;
           pattern.add(highest);
       pattern.add(highest-1);
       pattern.add(highest-2);
       pattern.add(highest - 2);
       pattern.add(highest- 4);
       }
       
       return straights;
    }
/**
 * sets hasFlush to true for calculating points later
 * adds the ranks of the flush to pattern for calculating points if has flush
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
        ArrayList<String> suits = new ArrayList<>();
        suits.add("Hearts");
        suits.add("Spades");
        suits.add("Diamonds");
        suits.add("Clubs");
        hasFlush = (ar[0] >= 5 || ar[1] >= 5 || ar[2] >= 5 || ar[3] >= 5);
        for (int i = 0; i < ar.length; i++){
            if (ar[i] >= 5){
               ArrayList<Integer> ranks = new ArrayList<>();
               for (Card c: hand){
                    if(c.getSuit().equals(suits.get(i))){
                        ranks.add(c.getRank());
                    }
               }
               for (int j = 0; j < ranks.size() - 1; j++){
                    for (int k = j + 1; k < ranks.size(); k++){
                        if (ranks.get(j) < ranks.get(k)){
                            int temp = ranks.get(j);
                            ranks.set(j, ranks.get(k));
                            ranks.set(k, temp);
                        }
                    }
               }
               for (int x = 0; x < 5 && x < ranks.size(); x++){
                pattern.add(ranks.get(x));
               }
               highest = ranks.get(0);
               break;
            }
        }
        return highest;
    }
/**
 * checks if player has a full house pattern
 * sets hasFullHouse to true for calculating points later
 * adds the rank of the three of a kind and the rank of the pair to pattern for calculating points if has full house
 * @return a number that is the rank of the three of a kind times 15 squared plus the rank of the pair times 15, if there is no full house, it returns -1
 * the returned number is used to calculate points 
 */
    public int[] hasFullHouse(){
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
            pattern.add(three);
            pattern.add(pair);
            return new int[]{three, pair};
        }
        hasFullHouse = false;
        
        return new int[]{-1, -1};
    }
/**
 * checks if player has a straight flush pattern
 * sets hasStraightFlush to true for calculating points later
 * adds the ranks of the straight flush to pattern for calculating points if has straight flush
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
                    ArrayList<String> suits = new ArrayList<>();
                    suits.add("Hearts");
                    suits.add("Spades");
                    suits.add("Diamonds");
                    suits.add("Clubs");
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
       for (Card c: hand){
        if (c.getRank() == highestss){
            pattern.add(c.getRank());
            pattern.add(c.getRank()-1);
            pattern.add(c.getRank()-2);
            pattern.add(c.getRank()-3);
            pattern.add(c.getRank()-4);
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
        ArrayList<String> suits = new ArrayList<>();
        suits.add("Hearts");
        suits.add("Spades");
        suits.add("Diamonds");
        suits.add("Clubs");
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
                return true;
                
            }
        }
        return false;
        
    }
    /**
 * finds the rank of the highest card in the player's hand not in the pattern for calculating points in case of tie breakers
 * @return rank of the ith highest card in the players hand not in the pattern
 * @param int i, finds the ith highest card in the players hand not in the pattern for calculating points in case of tie breakers
 */
    public int highestCardRank(int i){ 
        ArrayList<Integer> tibreaker = new ArrayList<>();
        for (Card c: hand){
            if (!pattern.contains(c.getRank())){
                tibreaker.add(c.getRank());
            }
        }
        for (int j = 0; j < tibreaker.size() - 1; j++){
            for (int k = j + 1; k < tibreaker.size(); k++){
                if (tibreaker.get(j) < tibreaker.get(k)){
                    int temp = tibreaker.get(j);
                    tibreaker.set(j, tibreaker.get(k));
                    tibreaker.set(k, temp);
                }
            }
        }  
        return tibreaker.get(tibreaker.size() - i);
    }

/**
 * checks pattern from highest to lowest and returns an array of six integers, 
 * the first integer is the rank of the hand, for example, royal flush is 9, straight flush is 8, four of a kind is 7, etc
 * the rest of the inttegers are the determinants of the hand, 
 * E.g. for two pairs
 * 2nd spot: highest pair rank
 * 3rd spot: second highest pair rank
 * 4th spot: highest rank not in pattern
 * 5th spot: second highest rank not in pattern
 * 6th spot: third highest rank not in pattern
 * @return calculated player point based on the ir hand 
 */
    public int[] calculatePoints(){
        pattern.clear();
        sixThings = new int[6];

        boolean hasRoyalFlush = hasRoyalFlush();
        int highest = hasStraightFlush();
        int highestt = hasFourOfAKind();
        int[] highestss =hasFullHouse();
        int highesttt = hasFlush(hand);
        ArrayList<Integer> highestttt = hasStraight();
        int highes = threeOfAKind();
        int[] high = hasTwoPair();
        int hih = hasPair();

        if (hasRoyalFlush){
            //9
            sixThings[0] = 9;
            return sixThings;
            
        } else if (hasStraightFlush){
            //8
            sixThings[0] = 8;
            sixThings[1] = highest; // has 5 cards already in pattern
            return sixThings;
            
        }else if (hasFourOfAKind){
            //7
            sixThings[0] = 7;
            sixThings[1] = highestt;
            sixThings[2] = highestCardRank(1); // has four cards in pattern, need extra to make 5 cards
            return sixThings;
        } else if (hasFullHouse){
            //6
            sixThings[0] = 6;
            sixThings[1] = highestss[0];
            sixThings[2] = highestss[1]; // full house has 5 cards already
            return sixThings;
        }else if (hasFlush){
           //5
            sixThings[0] = 5;
            sixThings[1] = highesttt;
            sixThings[2] = pattern.get(pattern.size() - 2);
            sixThings[3] = pattern.get(pattern.size() - 3);
            sixThings[4] = pattern.get(pattern.size() - 4);
            sixThings[5] = pattern.get(pattern.size() - 5);
            return sixThings;
        } else if (hasStraight){
            //4
            sixThings[0] = 4;
            sixThings[1] = highestttt.get(highestttt.size() - 1);
            return sixThings;
        } else if (hasThreeOfAKind){
            //3
            sixThings[0] = 3;
            sixThings[1] = highes;
            sixThings[2] = highestCardRank(1);
            sixThings[3] = highestCardRank(2);
            return sixThings;
        } else if (hasTwoPair){
            //2
            sixThings[0] = 2;
            sixThings[1] = high[0];
            sixThings[2] = high[1];
            sixThings[3] = highestCardRank(1);
            return sixThings;
           
        }else if (hasPair){
            //1
            sixThings[0] = 1;
            sixThings[1] = hih;
            sixThings[2] = highestCardRank(1);
            sixThings[3] = highestCardRank(2);
            sixThings[4] = highestCardRank(3);
            return sixThings;
        } else{
            sixThings[0] = 0;
            sixThings[1] = highestCardRank(1);
            sixThings[2] = highestCardRank(2);
            sixThings[3] = highestCardRank(3);
            sixThings[4] = highestCardRank(4);
            sixThings[5] = highestCardRank(5);
            return sixThings;
        }
        
    }
}
