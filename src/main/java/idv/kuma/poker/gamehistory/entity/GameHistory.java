package idv.kuma.poker.gamehistory.entity;

import idv.kuma.poker.table.entity.HandResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameHistory {
    private String handId;
    private HandResult handResult;

    public static GameHistory create(String handId, HandResult handResult) {
        return new GameHistory(handId, handResult);
    }

    public static GameHistory restore(String handId, HandResult handResult) {
        return new GameHistory(handId, handResult);
    }
}