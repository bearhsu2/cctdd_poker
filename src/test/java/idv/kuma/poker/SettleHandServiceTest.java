package idv.kuma.poker;

import idv.kuma.poker.common.exception.EntityExistsException;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.gamehistory.usecase.GameHistoryRepository;
import idv.kuma.poker.table.entity.*;
import idv.kuma.poker.table.usecase.HandRepository;
import idv.kuma.poker.table.usecase.SettleHandService;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static idv.kuma.poker.table.entity.Number.*;
import static idv.kuma.poker.table.entity.Suit.HEART;
import static idv.kuma.poker.table.entity.Suit.SPADE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SettleHandServiceTest {

    @Autowired
    private HandRepository handRepository;
    @Autowired
    private GameHistoryRepository gameHistoryRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private SettleHandService settleHandService;

    @Test
    void should_retrieve_hand_settle_it_and_save_back() throws EntityExistsException {
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
        then_game_history_should_be_created("hand-1",
                HandResult.of(Map.of(
                        0, PlayerResult.of("user-1", 1),
                        1, PlayerResult.of("user-2", 2)
                )));
        then_wallet_balance_should_be("user-1", 600);
        then_wallet_balance_should_be("user-2", 500);
    }

    private void given_wallet(String playerId, long initialBalance) throws EntityExistsException {
        Wallet wallet = Wallet.create(playerId, initialBalance);
        walletRepository.save(wallet);
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

    private void then_game_history_should_be_created(String handId, HandResult expectedResult) {
        GameHistory gameHistory = gameHistoryRepository.findByHandId(handId);
        assertThat(gameHistory).isNotNull();
        assertThat(gameHistory.getHandId()).isEqualTo(handId);
        assertThat(gameHistory.getHandResult()).isEqualTo(expectedResult);
    }

    private void then_wallet_balance_should_be(String playerId, long expectedBalance) {
        Wallet wallet = walletRepository.findByPlayerId(playerId);
        assertThat(wallet.getBalance()).isEqualTo(expectedBalance);
    }
}