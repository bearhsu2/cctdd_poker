package idv.kuma.poker.table.entity;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class PokerComparator {

    public HandResult compare(List<String> userIds, List<HoleCards> holeCards, Board board) {
        List<Map.Entry<Integer, PokerHand>> sortedEntries = IntStream.range(0, holeCards.size())
                .mapToObj(i -> Map.entry(i, holeCards.get(i).findBestHand(board)))
                .sorted(Map.Entry.<Integer, PokerHand>comparingByValue().reversed())
                .toList();

        Map<Integer, PlayerResult> results = new HashMap<>();
        int currentRank = 1;
        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i > 0 && sortedEntries.get(i).getValue().compareTo(sortedEntries.get(i - 1).getValue()) != 0) {
                currentRank = i + 1;
            }
            int position = sortedEntries.get(i).getKey();
            results.put(position, PlayerResult.of(userIds.get(position), currentRank));
        }

        return HandResult.of(results);
    }

}