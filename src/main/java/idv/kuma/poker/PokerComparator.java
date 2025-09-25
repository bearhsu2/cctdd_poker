package idv.kuma.poker;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;

public class PokerComparator {

    // https://zh.wikipedia.org/zh-tw/%E6%92%B2%E5%85%8B%E7%89%8C%E5%9E%8B
    public int compare(Hand hand1, Hand hand2) {
        return hand1.compareTo(hand2);
    }

    public Hand findMaxCategory(List<Card> playerCards, List<Card> board) {
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

    private List<List<Card>> generateCombinations(List<Card> cards, int k) {
        List<List<Card>> result = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), result);
        return result;
    }

    private void generateCombinationsHelper(List<Card> cards, int k, int start, List<Card> current, List<List<Card>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < cards.size(); i++) {
            current.add(cards.get(i));
            generateCombinationsHelper(cards, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}