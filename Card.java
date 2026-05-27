package com.example;

/**
 * @author Vicky Qin
 * @date 5/27/2026
 * Classifies cards by rank, suit, and name. 
 */
public class Card implements Comparable<Object> {
    private int rank;
    private String suit;
/**
 * Creates a Card Object with inputted values of Rank rank and Suit suit
 * @param rank
 * @param suit
 */
    public Card(int rank, String suit){
        this.rank = rank;
        this.suit = suit;
    }
/**
 * Returns the rank of a Card.
 * @return rank
 */
    public int getRank(){
        return rank;
    }
/**
 * Returns the name of a Card.
 * @return rank of suit
 */
    public String getName(){
        if (rank <= 10)
            return "" + rank + " of " + suit;
        else if(rank == 11)
            return "Jack of " + suit;
        else if(rank == 12)
            return "Queen of " + suit;
        else if(rank == 13)
            return "King of " + suit;
        return "Ace of " + suit;
    }
/**
 * Returns the suit of a Card.
 * @return suit;
 */
    public String getSuit(){
        return suit;
    }
    
/**
 * Compares cards by rank.
 * @return Rank1-Rank2
 */
    public int compareTo(Object obj){
        return rank-((Card)obj).getRank();
    }
}
