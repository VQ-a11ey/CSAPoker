package com.example;
import java.util.ArrayList;

/**
 * @author Anna Chen
 */
public class Deck {
    private ArrayList<Card> cards;

    /**
     * Creates a new deck of 52 cards, with ranks 2-14 (where 11-14 are Jack, Queen, King, Ace) 
     * Suits Hearts, Spades, Diamonds, Clubs
     */
    public Deck() {
        cards = new ArrayList<Card>(52);
        for (int i = 2; i < 15; i++) {
            cards.add(new Card(i, "Hearts"));
            cards.add(new Card(i, "Spades"));
            cards.add(new Card(i, "Diamonds"));
            cards.add(new Card(i, "Clubs"));
        }
    }

    /**
     * @return a random card from the deck, and removes it from the deck
     */
    public Card chooseCard() {
        Card c = cards.remove((int) (Math.random() * cards.size()));
        return c;
    }
}
