package idv.kuma.poker;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.TWO)), List.of(Card.of(Suit.CLUB, Number.TWO)));

        then_result_is(0);
    }

    private void when_compare(java.util.List<Card> poker1, java.util.List<Card> poker2) {
        actual = sut.compare(poker1, poker2);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.TWO)), List.of(Card.of(Suit.CLUB, Number.THREE)));

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.THREE)), List.of(Card.of(Suit.CLUB, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.JACK)), List.of(Card.of(Suit.CLUB, Number.TEN)));

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.QUEEN)), List.of(Card.of(Suit.CLUB, Number.JACK)));

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.KING)), List.of(Card.of(Suit.CLUB, Number.QUEEN)));

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.ACE)), List.of(Card.of(Suit.CLUB, Number.KING)));

        then_result_is(1);
    }

    @Test
    public void _club_2_vs_diamond_2_makes_first_win() {

        when_compare(List.of(Card.of(Suit.CLUB, Number.TWO)), List.of(Card.of(Suit.DIAMOND, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_heart_2_makes_first_win() {

        when_compare(List.of(Card.of(Suit.SPADE, Number.TWO)), List.of(Card.of(Suit.HEART, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _heart_2_vs_club_2_makes_first_win() {

        when_compare(List.of(Card.of(Suit.HEART, Number.TWO)), List.of(Card.of(Suit.CLUB, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_diamond_2_makes_first_win() {

        when_compare(List.of(Card.of(Suit.SPADE, Number.TWO)), List.of(Card.of(Suit.DIAMOND, Number.TWO)));

        then_result_is(1);
    }


}