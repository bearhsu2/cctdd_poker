package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.table.entity.HandResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryService {
    private final GameHistoryRepository gameHistoryRepository;

    public void execute(String handId, HandResult handResult) {
        GameHistory gameHistory = GameHistory.create(handId, handResult);
        gameHistoryRepository.save(gameHistory);
    }
}