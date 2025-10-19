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
import idv.kuma.poker.table.entity.HandResult;
import idv.kuma.poker.table.entity.PlayerResult;
import idv.kuma.poker.table.entity.Hand;
import idv.kuma.poker.table.entity.HandStatus;
import idv.kuma.poker.table.usecase.SettleHandService;
import idv.kuma.poker.table.usecase.HandRepository;
import idv.kuma.poker.wallet.adapter.AddBalanceEventHandler;
import idv.kuma.poker.wallet.adapter.WalletRepositoryInMemory;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.AddBalanceService;
import idv.kuma.poker.wallet.usecase.WalletRepository;
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
    private final WalletRepository walletRepository = new WalletRepositoryInMemory();
    private final IdGenerator idGenerator = new IdGeneratorWithUUID();
    private final AddGameHistoryService addGameHistoryService = new AddGameHistoryService(gameHistoryRepository, idGenerator);
    private final AddBalanceService addBalanceService = new AddBalanceService(walletRepository);
    private final DomainEventHandler dummyDomainEventHandler = new DummyDomainEventHandler();
    private final DomainEventHandler addGameHistoryEventHandler = new AddGameHistoryEventHandler(addGameHistoryService);
    private final DomainEventHandler updateWalletEventHandler = new AddBalanceEventHandler(addBalanceService);
    private final DomainEventBus domainEventBus = new DomainEventBusInMemory(dummyDomainEventHandler, addGameHistoryEventHandler, updateWalletEventHandler);
    private final PokerComparator pokerComparator = new PokerComparator();
    private final SettleHandService settleHandService = new SettleHandService(handRepository, domainEventBus, pokerComparator);

    @Test
    void should_retrieve_hand_settle_it_and_save_back() {
        given_wallet("user-1", 500);
        given_wallet("user-2", 500);
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
        then_hand_settled_event_should_be_sent("hand-1", 100,
                HandResult.of(Map.of(
                        0, PlayerResult.of("user-1", 1),
                        1, PlayerResult.of("user-2", 2)
                )));
        then_game_history_should_be_created("hand-1",
                HandResult.of(Map.of(
                        0, PlayerResult.of("user-1", 1),
                        1, PlayerResult.of("user-2", 2)
                )));
        then_wallet_balance_should_be("user-1", 600);
        then_wallet_balance_should_be("user-2", 500);
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

    private void then_hand_settled_event_should_be_sent(String handId, int expectedBet, HandResult expectedResult) {
        DummyDomainEventHandler handler = (DummyDomainEventHandler) dummyDomainEventHandler;
        assertThat(handler.getReceivedEvents()).hasSize(1);
        assertThat(handler.getReceivedEvents().get(0).handId()).isEqualTo(handId);
        assertThat(handler.getReceivedEvents().get(0).bet()).isEqualTo(expectedBet);

        HandResult actualResult = handler.getReceivedEvents().get(0).handResult();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private void then_game_history_should_be_created(String handId, HandResult expectedResult) {
        GameHistory gameHistory = gameHistoryRepository.findByHandId(handId);
        assertThat(gameHistory).isNotNull();
        assertThat(gameHistory.getId()).isNotEmpty();
        assertThat(gameHistory.getHandId()).isEqualTo(handId);
        assertThat(gameHistory.getHandResult()).isEqualTo(expectedResult);
    }

    private void given_wallet(String playerId, long initialBalance) {
        Wallet wallet = Wallet.restore(idGenerator.generate(), playerId, 1, initialBalance);
        walletRepository.save(wallet);
    }

    private void then_wallet_balance_should_be(String playerId, long expectedBalance) {
        Wallet wallet = walletRepository.findByPlayerId(playerId);
        assertThat(wallet.getBalance()).isEqualTo(expectedBalance);
    }
}
