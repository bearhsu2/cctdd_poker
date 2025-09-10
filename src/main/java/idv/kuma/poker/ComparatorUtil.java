package idv.kuma.poker;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ComparatorUtil {
    
    public static <T extends Comparable<T>> int compareByHighestCards(List<T> cards1, List<T> cards2) {
        DBCUtil.require(() -> cards1.size() == cards2.size(), 
                "Card lists must have the same size for comparison, but got " + cards1.size() + " and " + cards2.size());
        
        List<T> sortedCards1 = cards1.stream().sorted(Collections.reverseOrder()).toList();
        List<T> sortedCards2 = cards2.stream().sorted(Collections.reverseOrder()).toList();
        
        return IntStream.range(0, sortedCards1.size())
                .map(i -> sortedCards1.get(i).compareTo(sortedCards2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
    }
}