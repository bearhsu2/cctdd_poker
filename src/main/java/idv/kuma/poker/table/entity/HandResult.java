package idv.kuma.poker.table.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HandResult {

    private final Map<Integer, PlayerResult> positionToResult;

    public static HandResult of(Map<Integer, PlayerResult> results) {
        return new HandResult(results);
    }

    public int getRank(int position) {
        return positionToResult.get(position).getRank();
    }

    public String getUserId(int position) {
        return positionToResult.get(position).getUserId();
    }

    public PlayerResult getRank1Winner() {
        return positionToResult.values().stream()
                .filter(result -> result.getRank() == 1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No rank-1 winner found"));
    }
}
