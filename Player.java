public class Player {
    String name;
    int chips;
    Card cardOne;
    Card cardTwo;
    int bet;

    public Player() {
        name = "";
        chips = 0;
        cardOne = null;
        cardTwo = null;
        bet = 0;
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

    public int[] bet(int toBet) { //will return [current, addToPot] values
        int moreChips = toBet - bet;
        if (moreChips <= chips) {
            bet = toBet;
            int[] result = {toBet, moreChips};
            chips -= moreChips;
            return result;
        } else { // all in
            System.out.println(name + " is going all in!");
            bet += chips;
            int[] result = {chips + bet, chips};
            chips = 0;
            return result;
        }
    }

    public void printPlayerCards() {
        System.out.println(cardOne.getName() + " and " + cardTwo.getName());
    }

}
