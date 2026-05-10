import java.util.ArrayList;

public class PlayerPoints extends Player{
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
        } // above this line is sorting stuff maybe create a mehtod??? idk
        int straight = 1;
        for (int i = 0; i < ranks.length - 1; i++){
            if (ranks[i] + 1 == ranks[i+1]){
                straight++;
                if (straight == 5){
                    return true;
                }
            } else straight = 1;
        }
        return false;
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
        if (hasFlush()){
            return highestCardOfStraight()/15 == 14;
        }
        return false;
        
    }
    public void sortingForStraights(int[] rank){
        for (int i = 0; i < hand.size(); i++){
            rank[i] = hand.get(i).getRank();
        }
        for (int i = 0; i < rank.length - 1; i++){
            for (int j = i + 1; j < rank.length; j++){
                if (rank[i] > rank[j]){
                    int temp = rank[i];
                    rank[i] = rank[j];
                    rank[j] = temp;
                }
            }
        } 
    }
    public int highestCardOfStraight(){ 
        int[] ranks = new int[hand.size()];
        sortingForStraights(ranks);
        int straight = 1;
        for (int i = 0; i < ranks.length - 1; i++){
            if (ranks[i] + 1 == ranks[i+1]){
                straight++;
                if (straight == 5){
                    return i * 15 + highestCardRank()/15;
                }
            } else straight = 1;
        }
        return 0;
    }
    //public int highestCardOfFlush(){ do we need this? also one for straightflush
    public int highestCardRank(){
        int max = hand.get(0).getRank();
        for (Card c: hand){
            if (c.getRank() > max){
                max = c.getRank();
            }
        }
        return max * 15;
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
        return pair * 15 + highestCardRank()/15;
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
        return three * 15 + highestCardRank()/15;
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
            if (count == 4) return rank * 15 + highestCardRank()/15
        }
        return 0;
    }
    public int calculatePoints(){
        if (hasRoyalFlush()){
            return 2250;
        }
        if (hasStraightFlush()){
            return 2000 + highestCardRank();
        }
        if (hasFourOfAKind()){
            return 1750 + getFourRank();
        }
        if (hasFullHouse()){
            return 1500 + getThreeRank();
        }
        if (hasFlush()){
            return 1250 + highestCardRank();
        }
        if (hasStraight()){
            return 1000 + highestCardOfStraight();
        }
        if (hasThreeOfAKind()){
            return 750 + getThreeRank();
        }
        if (hasTwoPair()){
            return 500 + getPairRank();
        }
        if (hasPair()){
            return 250 + getPairRank();
        }
        return highestCardRank();
    }

}
