package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    @Test
    public void _2_vs_2_makes_tie() {
        Poker poker1 = Poker.with(2);
        Poker poker2 = Poker.with(2);
        PokerComparator comparator = new PokerComparator();

        int result = comparator.compare(poker1, poker2);

        assertEquals(0, result);
    }


}