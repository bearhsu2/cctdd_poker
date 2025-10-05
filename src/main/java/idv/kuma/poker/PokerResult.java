package idv.kuma.poker;

import java.util.Map;

public class PokerResult {

    private final Map<Integer, Integer> positionToRank;

    public PokerResult(Map<Integer, Integer> positionToRank) {
        this.positionToRank = positionToRank;
    }

    public int getRank(int position) {
        return positionToRank.get(position);
    }
}
