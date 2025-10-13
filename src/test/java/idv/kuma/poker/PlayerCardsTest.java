package idv.kuma.poker;

import idv.kuma.poker.table.entity.Board;
import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.PokerHand;
import idv.kuma.poker.table.entity.Number;
import idv.kuma.poker.table.entity.PlayerCards;
import idv.kuma.poker.table.entity.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerCardsTest {

    @Test
    public void findBestHand_returns_straight_when_player_cards_complete_board_straight() {
        PlayerCards sut = PlayerCards.of(List.of(card(Suit.CLUB, Number.NINE), card(Suit.HEART, Number.TEN)));

        PokerHand result = sut.findBestHand(
                Board.of(List.of(card(Suit.SPADE, Number.FOUR), card(Suit.DIAMOND, Number.FIVE), card(Suit.CLUB, Number.SIX), card(Suit.HEART, Number.SEVEN), card(Suit.SPADE, Number.EIGHT)))
        );

        PokerHand expected = PokerHand.of(List.of(
                card(Suit.CLUB, Number.SIX),
                card(Suit.HEART, Number.SEVEN),
                card(Suit.SPADE, Number.EIGHT),
                card(Suit.CLUB, Number.NINE),
                card(Suit.HEART, Number.TEN)
        ));
        assertEquals(expected, result);
    }

    @Test
    public void findBestHand_returns_two_pair_when_player_cards_match_two_board_numbers() {
        PlayerCards sut = PlayerCards.of(List.of(card(Suit.DIAMOND, Number.FOUR), card(Suit.HEART, Number.FIVE)));

        PokerHand result = sut.findBestHand(
                Board.of(List.of(card(Suit.SPADE, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.HEART, Number.JACK), card(Suit.DIAMOND, Number.SEVEN), card(Suit.SPADE, Number.NINE)))
        );

        PokerHand expected = PokerHand.of(List.of(
                card(Suit.DIAMOND, Number.FOUR),
                card(Suit.SPADE, Number.FOUR),
                card(Suit.HEART, Number.FIVE),
                card(Suit.CLUB, Number.FIVE),
                card(Suit.HEART, Number.JACK)
        ));
        assertEquals(expected, result);
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

}