package idv.kuma.poker.table.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PokerResult {

    private final Map<Integer, Integer> positionToRank;

    public static PokerResult of(Map<Integer, Integer> positionToRank) {
        return new PokerResult(positionToRank);
    }

    public int getRank(int position) {
        return positionToRank.get(position);
    }
}
