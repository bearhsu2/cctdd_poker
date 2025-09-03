package idv.kuma.poker;

import java.util.ArrayList;
import java.util.List;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        List<Card> sortedCards1 = new ArrayList<>(pokers1);
        List<Card> sortedCards2 = new ArrayList<>(pokers2);
        sortedCards1.sort((a, b) -> b.compareTo(a));
        sortedCards2.sort((a, b) -> b.compareTo(a));
        
        for (int i = 0; i < 5; i++) {
            int comparison = sortedCards1.get(i).compareTo(sortedCards2.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}