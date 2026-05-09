import java.util.ArrayList;

public class PlayerPoints extends Player{
    //should we do this??? idk
    private int points;
    private ArrayList<Card> hand;

    public PlayerPoints(Card one, Card two , ArrayList<Card> cards){
        points = 0;
        this.hand = new ArrayList<Card>();
        hand.add(one);
        hand.add(two);
        for (Card c: cards){
            hand.add(c);
        }
    }
    public ArrayList<Card> getHand(){
        return hand;
    }
    public void setPoints(int points){
        this.points = points;
    }
    public int getPoints(){
        return points;
    }
    public boolean hasPair(){
        for (int i = 0; i < hand.size(); i++){
            int count = 0; 
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand.get(j).getRank()){
                    count++;
                }
            }
            if (count == 2){
                return true;
            }
        }
        return false;
    }
    public boolean  hasTwoPair(){
        int pairs = 0;
         for (int i = 0; i < hand.size(); i++){
            int count = 0; 
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand.get(j).getRank()){
                    count++;
                }
            }
            if (count == 2){
                pairs++;
            }
        }
        return pairs/2 == 2;
    }
    public boolean hasThreeOfAKind(){
        for (int i = 0; i < hand.size(); i++){
            int count = 0; 
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand.get(j).getRank()){
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
        }
        return false;
    }
    public boolean hasStraight(){

    }
    public boolean hasFlush(){
        int[] ar = new int[4];
        for (Card c: hand){
            if (c.getSuit() == "Hearts"){
                ar[0]++;
            }else if (c.getSuit() == "Spades"){
                ar[1]++;
            }else if (c.getSuit() == "Diamonds"){
                ar[2]++;
            }else{
                ar[3]++;
            }
        }
        return ar[0] >= 5 || ar[1] >= 5 || ar[2] >= 5 || ar[3] >= 5;
    }
    public boolean hasFullHouse(){
        boolean three = false;
        int threeNum = 0;
        boolean pair = false;
        for (int i = 0; i < hand.size(); i++){
            int count = 0;
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand .get(j).getRank()){
                    count++;
                }
            }
            if (count == 3){
                three = true;
                threeNum = i;
            }
        }
        for (int i = 0; i < hand.size(); i++){
            int count = 0;
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand .get(j).getRank()){
                    count++;
                }
            }
            if (count == 2 && i != threeNum){
                pair = true;
            }
        }
        return three && pair;
    }
    public boolean hasFourOfAKind(){
        for (int i = 0; i < hand.size(); i++){
            int count = 0;
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(i).getRank() == hand.get(j).getRank()){
                    count++;
                }
            }
            if (count == 4) {
                return true;
            }
        } 
        return false;
    }
    public boolean hasStraightFlush(){
        return hasStraight() && hasFlush();
    }
    public boolean hasRoyalFlush(){
        
    }
    public int highestCardRank(){
        int max = hand.get(0).getRank();
        for (Card c: hand){
            if (c.getRank() > max){
                max = c.getRank();
            }
        }
        return 15;
    }
    public int getPairRank(){
        int pair = 0;
        for (int i = 0; i < hand.size(); i++){
            int count = 0;
            int rank = hand.get(i).getRank();
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(j).getRank()==rank){
                    count++;
                }
            }
            if (count == 2 && rank > pair){
                pair = rank;
            }
        }
        return pair * 15 + highestCardRank();
    }
    public int getThreeRank(){
        int three = 0;
        for (int i = 0; i < hand.size(); i++){
            int count = 0; 
            int rank = hand.get(i).getRank();
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(j).getRank() == rank){
                    count++;
                }
            }
            if (count == 3 && rank > three){
                three = rank;
            }
        }
        return three * 15 + highestCardRank();
    }
    public int getFourRank(){
        for (int i = 0; i < hand.size(); i++){
            int count = 0;
            int rank = hand.get(i).getRank();
            for (int j = 0; j < hand.size(); j++){
                if (hand.get(j).getRank() == rank){
                    count++;
                }
            }
            if (count == 4) return rank * 15 + highestCardRank();
        }
        return 0;
    }
    public int calculatePoints(){
        if (hasRoyalFlush()){
            return 1350;
        }
        if (hasStraightFlush()){
            return 1200 + highestCardRank();
        }
        if (hasFourOfAKind()){
            return 1050 + getFourRank();
        }
        if (hasFullHouse()){
            return 900 + getThreeRank();
        }
        if (hasFlush()){
            return 750 + highestCardRank();
        }
        if (hasStraight()){
            return 600 + highestCardRank();
        }
        if (hasThreeOfAKind()){
            return 450 + getThreeRank();
        }
        if (hasTwoPair()){
            return 300 + getPairRank();
        }
        if (hasPair()){
            return 150 + getPairRank();
        }
        return highestCardRank();
    }

}
