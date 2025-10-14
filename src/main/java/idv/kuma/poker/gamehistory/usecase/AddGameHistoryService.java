package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.common.usecase.IdGenerator;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.table.entity.HandResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryService {
    private final GameHistoryRepository gameHistoryRepository;
    private final IdGenerator idGenerator;

    public void execute(String handId, HandResult handResult) {
        String id = idGenerator.generate();
        GameHistory gameHistory = GameHistory.create(id, handId, handResult);
        gameHistoryRepository.save(gameHistory);
    }
}