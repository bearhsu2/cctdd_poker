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
import idv.kuma.poker.table.adapter.HandRepositoryInMemory;
import idv.kuma.poker.table.entity.Board;
import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.HoleCards;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.PokerResult;
import idv.kuma.poker.table.entity.Hand;
import idv.kuma.poker.table.entity.HandStatus;
import idv.kuma.poker.table.usecase.SettleHandService;
import idv.kuma.poker.table.usecase.HandRepository;
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

public class SettleHandServiceTest {
    private final HandRepository handRepository = new HandRepositoryInMemory();
    private final GameHistoryRepository gameHistoryRepository = new GameHistoryRepositoryInMemory();
    private final IdGenerator idGenerator = new IdGeneratorWithUUID();
    private final AddGameHistoryService addGameHistoryService = new AddGameHistoryService(gameHistoryRepository, idGenerator);
    private final DomainEventHandler dummyDomainEventHandler = new DummyDomainEventHandler();
    private final DomainEventHandler addGameHistoryEventHandler = new AddGameHistoryEventHandler(addGameHistoryService);
    private final DomainEventBus domainEventBus = new DomainEventBusInMemory(dummyDomainEventHandler, addGameHistoryEventHandler);
    private final PokerComparator pokerComparator = new PokerComparator();
    private final SettleHandService settleHandService = new SettleHandService(handRepository, domainEventBus, pokerComparator);

    @Test
    void should_retrieve_hand_settle_it_and_save_back() {
        given_hand("hand-1",
                List.of("user-1", "user-2"),
                100,
                List.of(
                        HoleCards.of(List.of(Card.of(HEART, ACE), Card.of(HEART, KING))),
                        HoleCards.of(List.of(Card.of(SPADE, TWO), Card.of(SPADE, QUEEN)))
                ),
                Board.of(List.of(
                        Card.of(HEART, THREE),
                        Card.of(SPADE, THREE),
                        Card.of(HEART, TWO),
                        Card.of(SPADE, KING),
                        Card.of(HEART, QUEEN)
                )));

        when_settle("hand-1");

        then_hand_status_should_be("hand-1", HandStatus.SETTLED, 2);
        then_hand_settled_event_should_be_sent("hand-1", List.of("user-1", "user-2"), 100, PokerResult.of(Map.of(0, 1, 1, 2)));
        then_game_history_should_be_created("hand-1", PokerResult.of(Map.of(0, 1, 1, 2)));
    }

    private void given_hand(String handId, List<String> userIds, int bet, List<HoleCards> holeCards, Board board) {
        Hand hand = Hand.restore(handId, HandStatus.CREATED, 1, userIds, bet, holeCards, board);
        handRepository.save(hand);
    }

    private void when_settle(String handId) {
        settleHandService.settle(handId);
    }

    private void then_hand_status_should_be(String handId, HandStatus expectedStatus, int expectedVersion) {
        Hand hand = handRepository.findById(handId);
        assertThat(hand.getStatus()).isEqualTo(expectedStatus);
        assertThat(hand.getVersion()).isEqualTo(expectedVersion);
    }

    private void then_hand_settled_event_should_be_sent(String handId, List<String> expectedUserIds, int expectedBet, PokerResult expectedResult) {
        DummyDomainEventHandler handler = (DummyDomainEventHandler) dummyDomainEventHandler;
        assertThat(handler.getReceivedEvents()).hasSize(1);
        assertThat(handler.getReceivedEvents().get(0).handId()).isEqualTo(handId);
        assertThat(handler.getReceivedEvents().get(0).userIds()).isEqualTo(expectedUserIds);
        assertThat(handler.getReceivedEvents().get(0).bet()).isEqualTo(expectedBet);

        PokerResult actualResult = handler.getReceivedEvents().get(0).pokerResult();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private void then_game_history_should_be_created(String handId, PokerResult expectedResult) {
        GameHistory gameHistory = gameHistoryRepository.findByHandId(handId);
        assertThat(gameHistory).isNotNull();
        assertThat(gameHistory.getId()).isNotEmpty();
        assertThat(gameHistory.getHandId()).isEqualTo(handId);
        assertThat(gameHistory.getPokerResult()).isEqualTo(expectedResult);
    }
}
