package idv.kuma.poker;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PokerComparator {

    public Hand findMaxCategory(List<Card> playerCards, List<Card> board) {
        DBCUtil.require(() -> playerCards.size() == 2, "Player cards must be exactly 2 cards, but got " + playerCards.size());
        DBCUtil.require(() -> board.size() == 5, "Board must be exactly 5 cards, but got " + board.size());

        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerCards);
        allCards.addAll(board);

        List<List<Card>> allCombinations = generateCombinations(allCards, 5);

        Hand bestHand = null;
        for (List<Card> combination : allCombinations) {
            Hand currentHand = Hand.of(combination);
            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {
                bestHand = currentHand;
            }
        }

        return bestHand;
    }

    private List<List<Card>> generateCombinations(List<Card> cards, int targetCardsInHand) {
        return combinations(cards, targetCardsInHand).collect(Collectors.toList());
    }

    private Stream<List<Card>> combinations(List<Card> cards, int k) {
        if (k == 0) {
            return Stream.of(new ArrayList<>());
        }
        if (k > cards.size()) {
            return Stream.empty();
        }

        Card head = cards.get(0);
        List<Card> tail = cards.subList(1, cards.size());

        // Combinations including the first element
        Stream<List<Card>> withHead = combinations(tail, k - 1)
            .map(combination -> {
                List<Card> newCombination = new ArrayList<>();
                newCombination.add(head);
                newCombination.addAll(combination);
                return newCombination;
            });

        // Combinations not including the first element
        Stream<List<Card>> withoutHead = combinations(tail, k);

        return Stream.concat(withHead, withoutHead);
    }
}