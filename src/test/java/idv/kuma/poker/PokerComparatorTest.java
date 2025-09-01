package idv.kuma.poker;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        Card poker1 = Card.of(Suit.CLUB, Number.TWO);
        Card poker2 = Card.of(Suit.CLUB, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

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

        Card poker1 = Card.of(Suit.CLUB, Number.TWO);
        Card poker2 = Card.of(Suit.CLUB, Number.THREE);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.THREE);
        Card poker2 = Card.of(Suit.CLUB, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.JACK);
        Card poker2 = Card.of(Suit.CLUB, Number.TEN);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.QUEEN);
        Card poker2 = Card.of(Suit.CLUB, Number.JACK);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.KING);
        Card poker2 = Card.of(Suit.CLUB, Number.QUEEN);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.ACE);
        Card poker2 = Card.of(Suit.CLUB, Number.KING);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _club_2_vs_diamond_2_makes_first_win() {

        Card poker1 = Card.of(Suit.CLUB, Number.TWO);
        Card poker2 = Card.of(Suit.DIAMOND, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_heart_2_makes_first_win() {

        Card poker1 = Card.of(Suit.SPADE, Number.TWO);
        Card poker2 = Card.of(Suit.HEART, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _heart_2_vs_club_2_makes_first_win() {

        Card poker1 = Card.of(Suit.HEART, Number.TWO);
        Card poker2 = Card.of(Suit.CLUB, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_diamond_2_makes_first_win() {

        Card poker1 = Card.of(Suit.SPADE, Number.TWO);
        Card poker2 = Card.of(Suit.DIAMOND, Number.TWO);

        when_compare(List.of(poker1), List.of(poker2));

        then_result_is(1);
    }


}