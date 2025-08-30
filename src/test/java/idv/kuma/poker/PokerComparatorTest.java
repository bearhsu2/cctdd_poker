package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        CardNumber poker1 = CardNumber.TWO;
        CardNumber poker2 = CardNumber.TWO;

        when_compare(poker1, poker2);

        then_result_is(0);
    }

    private void when_compare(CardNumber poker1, CardNumber poker2) {
        actual = sut.compare(poker1, poker2);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        CardNumber poker1 = CardNumber.TWO;
        CardNumber poker2 = CardNumber.THREE;

        when_compare(poker1, poker2);

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        CardNumber poker1 = CardNumber.THREE;
        CardNumber poker2 = CardNumber.TWO;

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        CardNumber poker1 = CardNumber.JACK;
        CardNumber poker2 = CardNumber.TEN;

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        CardNumber poker1 = CardNumber.QUEEN;
        CardNumber poker2 = CardNumber.JACK;

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        CardNumber poker1 = CardNumber.KING;
        CardNumber poker2 = CardNumber.QUEEN;

        when_compare(poker1, poker2);

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        CardNumber poker1 = CardNumber.ACE;
        CardNumber poker2 = CardNumber.KING;

        when_compare(poker1, poker2);

        then_result_is(1);
    }


}