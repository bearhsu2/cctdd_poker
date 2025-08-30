package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.TWO);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(0);
    }

    private void when_compare(Card poker1, Card poker2) {
        actual = sut.compare(poker1, poker2);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.TWO);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.THREE);

        when_compare(poker1, poker2);

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.THREE);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.JACK);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.TEN);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.QUEEN);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.JACK);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.KING);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.QUEEN);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.ACE);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.KING);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _club_2_vs_diamond_2_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, CardNumber.TWO);
        Card poker2 = Card.of(Suit.DIAMOND, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_heart_2_makes_first_win() {

        Card poker1 = Card.of(Suit.SPADE, CardNumber.TWO);
        Card poker2 = Card.of(Suit.HEART, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _heart_2_vs_club_2_makes_first_win() {

        Card poker1 = Card.of(Suit.HEART, CardNumber.TWO);
        Card poker2 = Card.of(Suit.CLUB, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_diamond_2_makes_first_win() {

        Card poker1 = Card.of(Suit.SPADE, CardNumber.TWO);
        Card poker2 = Card.of(Suit.DIAMOND, CardNumber.TWO);

        when_compare(poker1, poker2);

        then_result_is(1);
    }


}