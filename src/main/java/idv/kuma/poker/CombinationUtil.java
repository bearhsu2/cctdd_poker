package idv.kuma.poker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CombinationUtil {

    public static <T> List<List<T>> generateCombinations(List<T> items, int targetSize) {
        return combinations(items, targetSize).toList();
    }

    private static <T> Stream<List<T>> combinations(List<T> items, int k) {
        if (k == 0) {
            return Stream.of(new ArrayList<>());
        }
        if (k > items.size()) {
            return Stream.empty();
        }

        T head = items.get(0);
        List<T> tail = items.subList(1, items.size());

        Stream<List<T>> withHead = combinations(tail, k - 1)
            .map(combination -> {
                List<T> newCombination = new ArrayList<>();
                newCombination.add(head);
                newCombination.addAll(combination);
                return newCombination;
            });

        Stream<List<T>> withoutHead = combinations(tail, k);

        return Stream.concat(withHead, withoutHead);
    }
}