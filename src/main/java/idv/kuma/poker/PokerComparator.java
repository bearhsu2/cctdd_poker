package idv.kuma.poker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerComparator {

    public PokerResult compare(List<PlayerCards> playerCards, Board board) {
        List<Map.Entry<Integer, Hand>> sortedEntries = IntStream.range(0, playerCards.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> playerCards.get(i).findBestHand(board)))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Hand>comparingByValue().reversed())
                .toList();

        Map<Integer, Integer> positionToRank = new HashMap<>();
        for (int rank = 0; rank < sortedEntries.size(); rank++) {
            positionToRank.put(sortedEntries.get(rank).getKey(), rank + 1);
        }

        return new PokerResult(positionToRank);
    }

}