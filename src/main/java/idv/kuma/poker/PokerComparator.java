package idv.kuma.poker;

public class PokerComparator {
    public int compare(Card poker1, Card poker2) {
        int cardNumberComparison = Integer.compare(poker1.getCardNumber().getNumber(), poker2.getCardNumber().getNumber());
        if (cardNumberComparison != 0) {
            return cardNumberComparison;
        }
        
        // When card numbers are equal, compare suits by weight: higher weight wins
        return Integer.compare(poker1.getSuit().getWeight(), poker2.getSuit().getWeight());
    }
}