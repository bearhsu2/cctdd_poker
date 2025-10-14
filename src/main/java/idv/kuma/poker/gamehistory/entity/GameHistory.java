package idv.kuma.poker.gamehistory.entity;

import idv.kuma.poker.table.entity.HandResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameHistory {
    private String id;
    private String handId;
    private HandResult handResult;
    private int version;

    public static GameHistory create(String id, String handId, HandResult handResult) {
        return new GameHistory(id, handId, handResult, 1);
    }

    public static GameHistory restore(String id, String handId, HandResult handResult, int version) {
        return new GameHistory(id, handId, handResult, version);
    }
}