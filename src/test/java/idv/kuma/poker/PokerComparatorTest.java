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

        actual = sut.compare(poker1, poker2);

        assertEquals(0, actual);
    }


}