package idv.kuma.poker.hand.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HandResult {

    private final List<PlayerResult> results;

    public static HandResult of(List<PlayerResult> results) {
        return new HandResult(results);
    }

    public int getRank(int position) {
        return results.stream()
                .filter(result -> result.position() == position)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No result found for position " + position))
                .rank();
    }

    public String getUserId(int position) {
        return results.stream()
                .filter(result -> result.position() == position)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No result found for position " + position))
                .userId();
    }

    public PlayerResult getRank1Winner() {
        return results.stream()
                .filter(result -> result.rank() == 1)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No rank-1 winner found"));
    }
}
