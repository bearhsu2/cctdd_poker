package idv.kuma.poker;

import java.util.List;
import java.util.stream.Stream;

public class PokerComparator {

    public Hand findMaxCategory(PlayerCards playerCards, Board board) {
        List<Card> allCards = Stream.concat(playerCards.getCards().stream(), board.getCards().stream()).toList();

        return CombinationUtil.generateCombinations(allCards, 5)
            .stream()
            .map(Hand::of)
            .max(Hand::compareTo)
            .orElseThrow();
    }

}