package idv.kuma.poker.table.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HandResult {

    private final Map<Integer, PlayerResult> results;

    public static HandResult of(Map<Integer, PlayerResult> results) {
        return new HandResult(results);
    }

    public int getRank(int position) {
        return results.get(position).getRank();
    }

    public String getUserId(int position) {
        return results.get(position).getUserId();
    }
}
