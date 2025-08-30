package idv.kuma.poker;

public class PokerComparator {
    public int compare(Card poker1, Card poker2) {
        int cardNumberComparison = Integer.compare(poker1.getCardNumber().getNumber(), poker2.getCardNumber().getNumber());
        if (cardNumberComparison != 0) {
            return cardNumberComparison;
        }
        
        // When card numbers are equal, compare suits (CLUB wins over DIAMOND)
        if (poker1.getSuit() == Suit.CLUB && poker2.getSuit() == Suit.DIAMOND) {
            return 1;
        }
        if (poker1.getSuit() == Suit.DIAMOND && poker2.getSuit() == Suit.CLUB) {
            return -1;
        }
        
        return 0;
    }
}