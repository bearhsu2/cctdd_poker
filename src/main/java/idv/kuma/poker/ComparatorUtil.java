package idv.kuma.poker;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ComparatorUtil {
    
    public static <T extends Comparable<T>> int compareByHighest(List<T> list1, List<T> list2) {
        DBCUtil.require(() -> list1.size() == list2.size(), 
                "Lists must have the same size for comparison, but got " + list1.size() + " and " + list2.size());
        
        List<T> sortedList1 = list1.stream().sorted(Collections.reverseOrder()).toList();
        List<T> sortedList2 = list2.stream().sorted(Collections.reverseOrder()).toList();
        
        return IntStream.range(0, sortedList1.size())
                .map(i -> sortedList1.get(i).compareTo(sortedList2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
    }
}