package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    @Test
    public void compare_returns_player1_rank1_when_player1_has_one_pair_and_player2_has_high_card() {
        PokerComparator sut = new PokerComparator();
        PlayerCards cards0 = PlayerCards.of(List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING)));
        PlayerCards cards1 = PlayerCards.of(List.of(card(Suit.DIAMOND, Number.ACE), card(Suit.SPADE, Number.JACK)));
        Board board = Board.of(List.of(
                card(Suit.CLUB, Number.TWO),
                card(Suit.DIAMOND, Number.THREE),
                card(Suit.HEART, Number.FOUR),
                card(Suit.SPADE, Number.FIVE),
                card(Suit.CLUB, Number.SEVEN)
        ));

        PokerResult result = sut.compare(List.of(cards0, cards1), board);

        assertEquals(1, result.getRank(0));
        assertEquals(2, result.getRank(1));
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

}
