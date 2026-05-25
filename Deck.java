package com.example;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>(52);
        for (int i = 2; i < 15; i++) {
            cards.add(new Card(i, "Hearts"));
        }
        for (int i = 2; i < 15; i++) {
            cards.add(new Card(i, "Spades"));
        }
        for (int i = 2; i < 15; i++) {
            cards.add(new Card(i, "Diamonds"));
        }
        for (int i = 2; i < 15; i++) {
            cards.add(new Card(i, "Clubs"));
        }
    }

    public Card chooseCard() {
        Card c = cards.remove((int) (Math.random() * cards.size()));
        return c;
    }
}
