package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        Poker poker1 = Poker.with(2);
        Poker poker2 = Poker.with(2);

        when_compare(poker1, poker2);

        then_result_is(0);
    }

    private void when_compare(Poker poker1, Poker poker2) {
        actual = sut.compare(poker1, poker2);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        Poker poker1 = Poker.with(2);
        Poker poker2 = Poker.with(3);

        when_compare(poker1, poker2);

        then_result_is(-1);
    }


}