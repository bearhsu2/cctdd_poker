package idv.kuma.poker;

import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.Number;
import idv.kuma.poker.table.entity.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardComparatorTest {

    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        when_compare(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.TWO));

        then_result_is(0);
    }

    private void when_compare(Card card1, Card card2) {
        actual = card1.compareTo(card2);
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        when_compare(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE));

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.TWO));

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.JACK), card(Suit.CLUB, Number.TEN));

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.QUEEN), card(Suit.CLUB, Number.JACK));

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.KING), card(Suit.CLUB, Number.QUEEN));

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.ACE), card(Suit.CLUB, Number.KING));

        then_result_is(1);
    }

    @Test
    public void _club_2_vs_diamond_2_makes_first_win() {

        when_compare(card(Suit.CLUB, Number.TWO), card(Suit.DIAMOND, Number.TWO));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_heart_2_makes_first_win() {

        when_compare(card(Suit.SPADE, Number.TWO), card(Suit.HEART, Number.TWO));

        then_result_is(1);
    }

    @Test
    public void _heart_2_vs_club_2_makes_first_win() {

        when_compare(card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.TWO));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_diamond_2_makes_first_win() {

        when_compare(card(Suit.SPADE, Number.TWO), card(Suit.DIAMOND, Number.TWO));

        then_result_is(1);
    }
}