package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.common.usecase.IdGenerator;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.table.entity.PokerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryService {
    private final GameHistoryRepository gameHistoryRepository;
    private final IdGenerator idGenerator;

    public void execute(String handId, PokerResult pokerResult) {
        String id = idGenerator.generate();
        GameHistory gameHistory = GameHistory.create(id, handId, pokerResult);
        gameHistoryRepository.save(gameHistory);
    }
}