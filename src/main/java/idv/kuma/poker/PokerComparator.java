package idv.kuma.poker;

import java.util.List;
import java.util.stream.Stream;

public class PokerComparator {

    public Hand findMaxCategory(PlayCards playCards, Board board) {
        DBCUtil.require(() -> playCards.getCards().size() == 2, "Player cards must be exactly 2 cards, but got " + playCards.getCards().size());
        DBCUtil.require(() -> board.getCards().size() == 5, "Board must be exactly 5 cards, but got " + board.getCards().size());

        List<Card> allCards = Stream.concat(playCards.getCards().stream(), board.getCards().stream()).toList();

        return CombinationUtil.generateCombinations(allCards, 5)
            .stream()
            .map(Hand::of)
            .max(Hand::compareTo)
            .orElseThrow();
    }

}