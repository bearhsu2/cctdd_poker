package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.table.entity.PokerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryService {
    private final GameHistoryRepository gameHistoryRepository;

    public void execute(String tableId, PokerResult pokerResult) {
        GameHistory gameHistory = GameHistory.create(tableId, pokerResult);
        gameHistoryRepository.save(gameHistory);
    }
}