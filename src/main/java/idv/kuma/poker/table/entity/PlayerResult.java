package idv.kuma.poker.table.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerResult {
    private final String userId;
    private final int rank;

    public static PlayerResult of(String userId, int rank) {
        return new PlayerResult(userId, rank);
    }
}