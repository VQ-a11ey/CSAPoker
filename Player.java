public class Player{
    String name;
    int chips;
    Card cardOne;
    Card cardTwo;
    public Player(){
        name = "";
        chips = 0;
        cardOne = null;
        cardTwo = null;
    }
    public boolean call (int call){
        chips -= call;
        return true;
    }
    public boolean check(){
        return true;
    }
    public void fold(){

    }
    public boolean raise(int raise){ // returns false if invalid
        if (raise > chips){
            return false;
        }
        else{
            chips -= raise;
            return true;
        }
    }
    public Card getCardOne(){
        return cardOne;
    }
    public Card getCardTwo(){
        return cardTwo;
    }
    public int getChips(){
        return chips;
    }
    public String getName(){
        return name;
    }
    public void setCardOne(Card one){
        cardOne = one;
    }
    public void setCardTwo(Card two){
        cardTwo = two;
    }
    public void setName(String name){
        this.name = name;
    }
    public void addChips(int added){
        chips += added;
    }
    public void printCards(){
        System.out.println(cardOne.getName() + " and " + cardTwo.getName());
    }
}