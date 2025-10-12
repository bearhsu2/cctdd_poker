package idv.kuma.poker;

import idv.kuma.poker.common.adapter.DomainEventBusInMemory;
import idv.kuma.poker.common.adapter.IdGeneratorWithUUID;
import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.common.usecase.IdGenerator;
import idv.kuma.poker.gamehistory.adapter.AddGameHistoryEventHandler;
import idv.kuma.poker.gamehistory.adapter.GameHistoryRepositoryInMemory;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.gamehistory.usecase.AddGameHistoryService;
import idv.kuma.poker.gamehistory.usecase.GameHistoryRepository;
import idv.kuma.poker.table.adapter.TableRepositoryInMemory;
import idv.kuma.poker.table.entity.Board;
import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.PlayerCards;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.PokerResult;
import idv.kuma.poker.table.entity.Table;
import idv.kuma.poker.table.entity.TableStatus;
import idv.kuma.poker.table.usecase.SettleTableService;
import idv.kuma.poker.table.usecase.TableRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static idv.kuma.poker.table.entity.Number.ACE;
import static idv.kuma.poker.table.entity.Number.KING;
import static idv.kuma.poker.table.entity.Number.QUEEN;
import static idv.kuma.poker.table.entity.Number.TWO;
import static idv.kuma.poker.table.entity.Number.THREE;
import static idv.kuma.poker.table.entity.Suit.HEART;
import static idv.kuma.poker.table.entity.Suit.SPADE;
import static org.assertj.core.api.Assertions.assertThat;

public class SettleTableServiceTest {
    private final TableRepository tableRepository = new TableRepositoryInMemory();
    private final GameHistoryRepository gameHistoryRepository = new GameHistoryRepositoryInMemory();
    private final IdGenerator idGenerator = new IdGeneratorWithUUID();
    private final AddGameHistoryService addGameHistoryService = new AddGameHistoryService(gameHistoryRepository, idGenerator);
    private final DomainEventHandler dummyDomainEventHandler = new DummyDomainEventHandler();
    private final DomainEventHandler addGameHistoryEventHandler = new AddGameHistoryEventHandler(addGameHistoryService);
    private final DomainEventBus domainEventBus = new DomainEventBusInMemory(dummyDomainEventHandler, addGameHistoryEventHandler);
    private final PokerComparator pokerComparator = new PokerComparator();
    private final SettleTableService settleTableService = new SettleTableService(tableRepository, domainEventBus, pokerComparator);

    @Test
    void should_retrieve_table_settle_it_and_save_back() {
        given_table("table-1",
                List.of(
                        PlayerCards.of(List.of(Card.of(HEART, ACE), Card.of(HEART, KING))),
                        PlayerCards.of(List.of(Card.of(SPADE, TWO), Card.of(SPADE, QUEEN)))
                ),
                Board.of(List.of(
                        Card.of(HEART, THREE),
                        Card.of(SPADE, THREE),
                        Card.of(HEART, TWO),
                        Card.of(SPADE, KING),
                        Card.of(HEART, QUEEN)
                )));

        when_settle("table-1");

        then_table_status_should_be("table-1", TableStatus.SETTLED, 2);
        then_table_settled_event_should_be_sent("table-1", PokerResult.of(Map.of(0, 1, 1, 2)));
        then_game_history_should_be_created("table-1", PokerResult.of(Map.of(0, 1, 1, 2)));
    }

    private void given_table(String tableId, List<PlayerCards> playerCards, Board board) {
        Table table = Table.restore(tableId, TableStatus.CREATED, 1, playerCards, board);
        tableRepository.save(table);
    }

    private void when_settle(String tableId) {
        settleTableService.settle(tableId);
    }

    private void then_table_status_should_be(String tableId, TableStatus expectedStatus, int expectedVersion) {
        Table table = tableRepository.findById(tableId);
        assertThat(table.getStatus()).isEqualTo(expectedStatus);
        assertThat(table.getVersion()).isEqualTo(expectedVersion);
    }

    private void then_table_settled_event_should_be_sent(String tableId, PokerResult expectedResult) {
        DummyDomainEventHandler handler = (DummyDomainEventHandler) dummyDomainEventHandler;
        assertThat(handler.getReceivedEvents()).hasSize(1);
        assertThat(handler.getReceivedEvents().get(0).tableId()).isEqualTo(tableId);

        PokerResult actualResult = handler.getReceivedEvents().get(0).pokerResult();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private void then_game_history_should_be_created(String tableId, PokerResult expectedResult) {
        GameHistory gameHistory = gameHistoryRepository.findByTableId(tableId);
        assertThat(gameHistory).isNotNull();
        assertThat(gameHistory.getId()).isNotEmpty();
        assertThat(gameHistory.getTableId()).isEqualTo(tableId);
        assertThat(gameHistory.getPokerResult()).isEqualTo(expectedResult);
    }
}
