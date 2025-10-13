package idv.kuma.poker;

import idv.kuma.poker.table.entity.Board;
import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.Number;
import idv.kuma.poker.table.entity.HoleCards;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.PokerResult;
import idv.kuma.poker.table.entity.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    @Test
    public void compare_returns_player1_rank1_when_player1_has_one_pair_and_player2_has_high_card() {
        PokerComparator sut = new PokerComparator();
        HoleCards cards0 = HoleCards.of(List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING)));
        HoleCards cards1 = HoleCards.of(List.of(card(Suit.DIAMOND, Number.ACE), card(Suit.SPADE, Number.JACK)));
        Board board = Board.of(List.of(
                card(Suit.CLUB, Number.THREE),
                card(Suit.DIAMOND, Number.FOUR),
                card(Suit.HEART, Number.FIVE),
                card(Suit.SPADE, Number.SIX),
                card(Suit.CLUB, Number.EIGHT)
        ));

        PokerResult result = sut.compare(List.of(cards0, cards1), board);

        assertEquals(1, result.getRank(0));
        assertEquals(2, result.getRank(1));
    }

    @Test
    public void compare_returns_same_rank_for_tied_players() {
        PokerComparator sut = new PokerComparator();
        HoleCards cards0 = HoleCards.of(List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.KING)));
        HoleCards cards1 = HoleCards.of(List.of(card(Suit.DIAMOND, Number.TWO), card(Suit.SPADE, Number.THREE)));
        HoleCards cards2 = HoleCards.of(List.of(card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.THREE)));
        Board board = Board.of(List.of(
                card(Suit.SPADE, Number.FOUR),
                card(Suit.DIAMOND, Number.FIVE),
                card(Suit.HEART, Number.SIX),
                card(Suit.CLUB, Number.NINE),
                card(Suit.SPADE, Number.TEN)
        ));

        PokerResult result = sut.compare(List.of(cards0, cards1, cards2), board);

        assertEquals(3, result.getRank(0));
        assertEquals(1, result.getRank(1));
        assertEquals(1, result.getRank(2));
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

}
