package idv.kuma.poker;

import java.util.ArrayList;
import java.util.List;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        List<Card> remainingCards1 = new ArrayList<>(pokers1);
        List<Card> remainingCards2 = new ArrayList<>(pokers2);
        
        for (int i = 0; i < 5; i++) {
            Card maxCard1 = remainingCards1.stream().max(Card::compareTo).get();
            Card maxCard2 = remainingCards2.stream().max(Card::compareTo).get();
            int comparison = maxCard1.compareTo(maxCard2);
            if (comparison != 0) {
                return comparison;
            }
            remainingCards1.remove(maxCard1);
            remainingCards2.remove(maxCard2);
        }
        
        return 0;
    }
}