package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.gamehistory.entity.GameHistory;

import java.util.Optional;

public interface GameHistoryRepository {
    void save(GameHistory gameHistory);

    Optional<GameHistory> findByHandId(String handId);
}