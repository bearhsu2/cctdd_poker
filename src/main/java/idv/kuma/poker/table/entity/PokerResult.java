package idv.kuma.poker.table.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class PokerResult {

    private final Map<Integer, Integer> positionToRank;

    public int getRank(int position) {
        return positionToRank.get(position);
    }
}
