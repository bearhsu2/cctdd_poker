package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.gamehistory.entity.GameHistory;

public interface GameHistoryRepository {
    void save(GameHistory gameHistory);

    GameHistory findByHandId(String handId);
}