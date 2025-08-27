package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    @Test
    public void testPokerComparatorWith2vs2() {
        Poker poker1 = new Poker(2);
        Poker poker2 = new Poker(2);
        PokerComparator comparator = new PokerComparator();

        int result = comparator.compare(poker1, poker2);

        assertEquals(0, result);
    }


}