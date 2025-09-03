package idv.kuma.poker;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        List<Card> sortedCards1 = pokers1.stream().sorted(Collections.reverseOrder()).toList();
        List<Card> sortedCards2 = pokers2.stream().sorted(Collections.reverseOrder()).toList();

        return IntStream.range(0, sortedCards1.size())
                .map(i -> sortedCards1.get(i).compareTo(sortedCards2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
    }
}