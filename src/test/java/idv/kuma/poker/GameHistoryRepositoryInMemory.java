package idv.kuma.poker;

import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.gamehistory.usecase.GameHistoryRepository;

import java.util.HashMap;
import java.util.Map;

public class GameHistoryRepositoryInMemory implements GameHistoryRepository {
    private final Map<String, GameHistory> gameHistories = new HashMap<>();

    @Override
    public void save(GameHistory gameHistory) {
        gameHistories.put(gameHistory.getTableId(), gameHistory);
    }

    @Override
    public GameHistory findByTableId(String tableId) {
        return gameHistories.get(tableId);
    }
}