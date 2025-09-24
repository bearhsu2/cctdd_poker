package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();

    @Test
    public void findMaxCategory_returns_non_null_hand() {

        Hand result = sut.findMaxCategory(
                List.of(card(Suit.CLUB, Number.NINE), card(Suit.HEART, Number.TEN)),
                List.of(card(Suit.SPADE, Number.FOUR), card(Suit.DIAMOND, Number.FIVE), card(Suit.CLUB, Number.SIX), card(Suit.HEART, Number.SEVEN), card(Suit.SPADE, Number.EIGHT))
        );

        assertNotNull(result);
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

}