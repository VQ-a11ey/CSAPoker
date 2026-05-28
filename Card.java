public class Card implements Comparable<Object> {
    private int rank;
    private String suit;

    public Card(int i, String s){
        rank = i;
        suit = s;
    }

    public int getRank(){
        return rank;
    }

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

    public String getSuit(){
        return suit;
    }
    
    
    public int compareTo(Object obj){
        return rank-((Card)obj).getRank();
    }
}
