package idv.kuma.poker;

import java.util.Collections;
import java.util.List;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        List<Card> sortedCards1 = pokers1.stream().sorted(Collections.reverseOrder()).toList();
        List<Card> sortedCards2 = pokers2.stream().sorted(Collections.reverseOrder()).toList();



        for (int i = 0; i < 5; i++) {
            int comparison = sortedCards1.get(i).compareTo(sortedCards2.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}