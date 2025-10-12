package idv.kuma.poker.gamehistory.entity;

import idv.kuma.poker.table.entity.PokerResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameHistory {
    private String id;
    private String tableId;
    private PokerResult pokerResult;
    private int version;

    public static GameHistory create(String id, String tableId, PokerResult pokerResult) {
        return new GameHistory(id, tableId, pokerResult, 1);
    }

    public static GameHistory restore(String id, String tableId, PokerResult pokerResult, int version) {
        return new GameHistory(id, tableId, pokerResult, version);
    }
}