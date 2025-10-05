package idv.kuma.poker;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerComparator {

    public PokerResult compare(List<PlayerCards> playerCards, Board board) {
        List<Hand> hands = playerCards.stream()
                .map(cards -> cards.findBestHand(board))
                .toList();

        List<Integer> sortedPositions = IntStream.range(0, hands.size())
                .boxed()
                .sorted((i1, i2) -> hands.get(i2).compareTo(hands.get(i1)))
                .toList();

        Map<Integer, Integer> positionToRank = IntStream.range(0, sortedPositions.size())
                .boxed()
                .collect(Collectors.toMap(
                        sortedPositions::get,
                        i -> i + 1
                ));

        return new PokerResult(positionToRank);
    }

}