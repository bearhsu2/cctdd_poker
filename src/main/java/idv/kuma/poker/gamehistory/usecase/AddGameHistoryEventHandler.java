package idv.kuma.poker.gamehistory.usecase;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.table.entity.TableSettledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryEventHandler implements DomainEventHandler {
    private final GameHistoryRepository gameHistoryRepository;

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof TableSettledEvent tableSettledEvent) {
            GameHistory gameHistory = GameHistory.create(
                    tableSettledEvent.getTableId(),
                    tableSettledEvent.getPokerResult()
            );
            gameHistoryRepository.save(gameHistory);
        }
    }
}
