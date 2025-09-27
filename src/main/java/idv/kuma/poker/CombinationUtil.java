package idv.kuma.poker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombinationUtil {

    public static List<List<Card>> generateCombinations(List<Card> cards, int targetCardsInHand) {
        return combinations(cards, targetCardsInHand).collect(Collectors.toList());
    }

    private static Stream<List<Card>> combinations(List<Card> cards, int k) {
        if (k == 0) {
            return Stream.of(new ArrayList<>());
        }
        if (k > cards.size()) {
            return Stream.empty();
        }

        Card head = cards.get(0);
        List<Card> tail = cards.subList(1, cards.size());

        Stream<List<Card>> withHead = combinations(tail, k - 1)
            .map(combination -> {
                List<Card> newCombination = new ArrayList<>();
                newCombination.add(head);
                newCombination.addAll(combination);
                return newCombination;
            });

        Stream<List<Card>> withoutHead = combinations(tail, k);

        return Stream.concat(withHead, withoutHead);
    }
}