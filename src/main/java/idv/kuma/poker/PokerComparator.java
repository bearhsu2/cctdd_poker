package idv.kuma.poker;

public class PokerComparator {
    public int compare(Card poker1, Card poker2) {
        return Integer.compare(poker1.getCardNumber().getNumber(), poker2.getCardNumber().getNumber());
    }
}