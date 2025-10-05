package idv.kuma.poker;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PokerComparator {

    public PokerResult compare(List<PlayerCards> playerCards, Board board) {
        Map<Integer, Hand> positionToHand = new HashMap<>();
        for (int i = 0; i < playerCards.size(); i++) {
            positionToHand.put(i, playerCards.get(i).findBestHand(board));
        }

        List<Map.Entry<Integer, Hand>> sortedEntries = positionToHand.entrySet().stream()
                .sorted(Map.Entry.<Integer, Hand>comparingByValue().reversed())
                .toList();

        Map<Integer, Integer> positionToRank = new HashMap<>();
        for (int rank = 0; rank < sortedEntries.size(); rank++) {
            positionToRank.put(sortedEntries.get(rank).getKey(), rank + 1);
        }

        return new PokerResult(positionToRank);
    }

}