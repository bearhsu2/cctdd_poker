package idv.kuma.poker;

import java.util.List;
import java.util.ArrayList;

public class PokerComparator {

    public Hand findMaxCategory(List<Card> playerCards, List<Card> board) {
        DBCUtil.require(() -> playerCards.size() == 2, "Player cards must be exactly 2 cards, but got " + playerCards.size());
        DBCUtil.require(() -> board.size() == 5, "Board must be exactly 5 cards, but got " + board.size());

        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerCards);
        allCards.addAll(board);

        return CombinationUtil.generateCombinations(allCards, 5)
            .stream()
            .map(Hand::of)
            .max(Hand::compareTo)
            .orElseThrow();
    }

}