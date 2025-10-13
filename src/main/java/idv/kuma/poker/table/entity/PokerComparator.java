package idv.kuma.poker.table.entity;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class PokerComparator {

    public PokerResult compare(List<PlayerCards> playerCards, Board board) {
        List<Map.Entry<Integer, PokerHand>> sortedEntries = IntStream.range(0, playerCards.size())
                .mapToObj(i -> Map.entry(i, playerCards.get(i).findBestHand(board)))
                .sorted(Map.Entry.<Integer, PokerHand>comparingByValue().reversed())
                .toList();

        Map<Integer, Integer> positionToRank = new HashMap<>();
        int currentRank = 1;
        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i > 0 && sortedEntries.get(i).getValue().compareTo(sortedEntries.get(i - 1).getValue()) != 0) {
                currentRank = i + 1;
            }
            positionToRank.put(sortedEntries.get(i).getKey(), currentRank);
        }

        return PokerResult.of(positionToRank);
    }

}