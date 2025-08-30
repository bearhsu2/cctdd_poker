package idv.kuma.poker;

public class PokerComparator {
    public int compare(Card poker1, Card poker2) {
        int cardNumberComparison = Integer.compare(poker1.getCardNumber().getNumber(), poker2.getCardNumber().getNumber());
        if (cardNumberComparison != 0) {
            return cardNumberComparison;
        }
        
        // When card numbers are equal, compare suits: SPADE > HEART > CLUB > DIAMOND
        // Since enum ordinals are: SPADE=0, HEART=1, CLUB=2, DIAMOND=3
        // Lower ordinal = higher suit rank, so we reverse the comparison
        return Integer.compare(poker2.getSuit().ordinal(), poker1.getSuit().ordinal());
    }
}