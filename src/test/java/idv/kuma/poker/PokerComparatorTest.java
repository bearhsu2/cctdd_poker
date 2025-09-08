package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void _2_vs_2_makes_tie() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.TWO)), five_identical_cards(card(Suit.CLUB, Number.TWO)));

        then_result_is(0);
    }

    private void when_compare(java.util.List<Card> poker1, java.util.List<Card> poker2) {
        actual = sut.compare(Hand.of(poker1), Hand.of(poker2));
    }

    private List<Card> five_identical_cards(Card card) {
        return List.of(card, card, card, card, card);
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void _2_vs_3_makes_first_lose() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.TWO)), five_identical_cards(card(Suit.CLUB, Number.THREE)));

        then_result_is(-1);
    }

    @Test
    public void _3_vs_2_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.THREE)), five_identical_cards(card(Suit.CLUB, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _J_vs_10_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.JACK)), five_identical_cards(card(Suit.CLUB, Number.TEN)));

        then_result_is(1);
    }

    @Test
    public void _Q_vs_J_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.QUEEN)), five_identical_cards(card(Suit.CLUB, Number.JACK)));

        then_result_is(1);
    }

    @Test
    public void _K_vs_Q_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.KING)), five_identical_cards(card(Suit.CLUB, Number.QUEEN)));

        then_result_is(1);
    }

    @Test
    public void _A_vs_K_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.ACE)), five_identical_cards(card(Suit.CLUB, Number.KING)));

        then_result_is(1);
    }

    @Test
    public void _club_2_vs_diamond_2_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.CLUB, Number.TWO)), five_identical_cards(card(Suit.DIAMOND, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_heart_2_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.SPADE, Number.TWO)), five_identical_cards(card(Suit.HEART, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _heart_2_vs_club_2_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.HEART, Number.TWO)), five_identical_cards(card(Suit.CLUB, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void _spade_2_vs_diamond_2_makes_first_win() {

        when_compare(five_identical_cards(card(Suit.SPADE, Number.TWO)), five_identical_cards(card(Suit.DIAMOND, Number.TWO)));

        then_result_is(1);
    }

    @Test
    public void when_comparing_two_lists_compare_largest_card() {

        when_compare(
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.HEART, Number.ACE)),
            List.of(card(Suit.CLUB, Number.KING), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE), card(Suit.SPADE, Number.QUEEN))
        );

        then_result_is(1);
    }

    @Test
    public void when_largest_cards_are_identical_compare_second_largest_cards() {

        when_compare(
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.ACE)),
            List.of(card(Suit.CLUB, Number.QUEEN), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE), card(Suit.SPADE, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void when_largest_and_second_largest_cards_are_identical_compare_third_largest_cards() {

        when_compare(
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.JACK), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.ACE)),
            List.of(card(Suit.CLUB, Number.TEN), card(Suit.CLUB, Number.SEVEN), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.NINE), card(Suit.SPADE, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void when_first_three_largest_cards_are_identical_then_compare_forth_largest_cards() {

        when_compare(
            List.of(card(Suit.CLUB, Number.NINE), card(Suit.CLUB, Number.JACK), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.ACE)),
            List.of(card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.JACK), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.SEVEN), card(Suit.SPADE, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void when_first_four_largest_cards_are_identical_then_compare_fifth_largest_cards() {

        when_compare(
            List.of(card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.JACK), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.ACE)),
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.JACK), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void one_pair_beats_high_card() {

        when_compare(
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE)),
            List.of(card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE), card(Suit.CLUB, Number.TEN), card(Suit.SPADE, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void high_card_loses_to_one_pair() {

        when_compare(
            List.of(card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE), card(Suit.CLUB, Number.TEN), card(Suit.SPADE, Number.ACE)),
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE))
        );

        then_result_is(-1);
    }

    @Test
    public void one_pair_with_larger_pair_wins() {

        when_compare(
            List.of(card(Suit.CLUB, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SIX)),
            List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE))
        );

        then_result_is(1);
    }


}