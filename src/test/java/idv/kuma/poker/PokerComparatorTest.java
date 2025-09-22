package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerComparatorTest {

    private PokerComparator sut = new PokerComparator();
    private int actual;

    @Test
    public void when_comparing_two_lists_compare_largest_card() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.HEART, Number.ACE)),
                List.of(card(Suit.CLUB, Number.KING), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE), card(Suit.SPADE, Number.QUEEN))
        );

        then_result_is(1);
    }

    private void when_compare(java.util.List<Card> poker1, java.util.List<Card> poker2) {
        actual = sut.compare(Hand.of(poker1), Hand.of(poker2));
    }

    private Card card(Suit suit, Number number) {
        return Card.of(suit, number);
    }

    private void then_result_is(int expected) {
        assertEquals(expected, actual);
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

    @Test
    public void one_pair_with_same_number_but_higher_suit_wins() {

        when_compare(
                List.of(card(Suit.HEART, Number.THREE), card(Suit.SPADE, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SIX)),
                List.of(card(Suit.CLUB, Number.THREE), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE))
        );

        then_result_is(1);
    }

    @Test
    public void one_pair_with_identical_highest_pair_cards_compares_second_pair_card() {

        when_compare(
                List.of(card(Suit.SPADE, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SIX)),
                List.of(card(Suit.SPADE, Number.THREE), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE))
        );

        then_result_is(1);
    }

    @Test
    public void one_pair_with_identical_pairs_compares_kickers_from_largest_to_smallest() {

        when_compare(
                List.of(card(Suit.SPADE, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.KING)),
                List.of(card(Suit.SPADE, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE))
        );

        then_result_is(1);
    }

    @Test
    public void two_pair_beats_one_pair() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.CLUB, Number.FOUR)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE), card(Suit.CLUB, Number.KING), card(Suit.CLUB, Number.QUEEN), card(Suit.CLUB, Number.JACK))
        );

        then_result_is(1);
    }

    @Test
    public void three_of_a_kind_beats_two_pair() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE), card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.QUEEN))
        );

        then_result_is(1);
    }

    @Test
    public void three_of_a_kind_vs_three_of_a_kind_higher_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE)),
                List.of(card(Suit.CLUB, Number.QUEEN), card(Suit.HEART, Number.QUEEN), card(Suit.DIAMOND, Number.QUEEN), card(Suit.CLUB, Number.ACE), card(Suit.CLUB, Number.FOUR))
        );

        then_result_is(1);
    }

    @Test
    public void three_of_a_kind_vs_three_of_a_kind_same_rank_higher_kicker_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.CLUB, Number.ACE), card(Suit.CLUB, Number.THREE)),
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.CLUB, Number.QUEEN), card(Suit.CLUB, Number.FOUR))
        );

        then_result_is(1);
    }

    @Test
    public void straight_beats_three_of_a_kind() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.THREE), card(Suit.DIAMOND, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.SIX)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE), card(Suit.DIAMOND, Number.ACE), card(Suit.CLUB, Number.KING), card(Suit.CLUB, Number.QUEEN))
        );

        then_result_is(1);
    }

    @Test
    public void five_high_straight_beats_three_of_a_kind() {

        when_compare(
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.SPADE, Number.FIVE)),
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.CLUB, Number.QUEEN), card(Suit.CLUB, Number.JACK))
        );

        then_result_is(1);
    }

    @Test
    public void higher_straight_beats_lower_straight() {

        when_compare(
                List.of(card(Suit.CLUB, Number.FIVE), card(Suit.HEART, Number.SIX), card(Suit.DIAMOND, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.SPADE, Number.NINE)),
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.THREE), card(Suit.DIAMOND, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.SIX))
        );

        then_result_is(1);
    }

    @Test
    public void six_high_straight_beats_five_high_straight() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.THREE), card(Suit.DIAMOND, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.SPADE, Number.SIX)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.SPADE, Number.FIVE))
        );

        then_result_is(1);
    }

    @Test
    public void straight_with_identical_numbers_makes_tie() {

        when_compare(
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.HEART, Number.FIVE)),
                List.of(card(Suit.SPADE, Number.ACE), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE))
        );

        then_result_is(0);
    }

    @Test
    public void flush_beats_straight() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.SIX), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.TEN)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.THREE), card(Suit.SPADE, Number.FOUR), card(Suit.CLUB, Number.FIVE))
        );

        then_result_is(1);
    }

    @Test
    public void flush_with_larger_high_card_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.SIX), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.KING)),
                List.of(card(Suit.HEART, Number.THREE), card(Suit.HEART, Number.FIVE), card(Suit.HEART, Number.SEVEN), card(Suit.HEART, Number.NINE), card(Suit.HEART, Number.QUEEN))
        );

        then_result_is(1);
    }

    @Test
    public void flush_hands_with_identical_numbers_but_different_suits_are_equal() {

        when_compare(
                List.of(card(Suit.DIAMOND, Number.TEN), card(Suit.DIAMOND, Number.EIGHT), card(Suit.DIAMOND, Number.SEVEN), card(Suit.DIAMOND, Number.SIX), card(Suit.DIAMOND, Number.FIVE)),
                List.of(card(Suit.SPADE, Number.TEN), card(Suit.SPADE, Number.EIGHT), card(Suit.SPADE, Number.SEVEN), card(Suit.SPADE, Number.SIX), card(Suit.SPADE, Number.FIVE))
        );

        then_result_is(0);
    }

    @Test
    public void full_house_beats_flush() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.HEART, Number.THREE)),
                List.of(card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.NINE), card(Suit.CLUB, Number.JACK), card(Suit.CLUB, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void full_house_vs_full_house_higher_three_of_a_kind_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.FIVE), card(Suit.HEART, Number.FIVE), card(Suit.DIAMOND, Number.FIVE), card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO)),
                List.of(card(Suit.CLUB, Number.THREE), card(Suit.HEART, Number.THREE), card(Suit.DIAMOND, Number.THREE), card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void full_house_vs_full_house_identical_triplet_and_pair_ranks_tie() {

        when_compare(
                List.of(card(Suit.CLUB, Number.SEVEN), card(Suit.HEART, Number.SEVEN), card(Suit.DIAMOND, Number.SEVEN), card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING)),
                List.of(card(Suit.SPADE, Number.SEVEN), card(Suit.DIAMOND, Number.SEVEN), card(Suit.HEART, Number.SEVEN), card(Suit.DIAMOND, Number.KING), card(Suit.SPADE, Number.KING))
        );

        then_result_is(0);
    }

    @Test
    public void full_house_vs_full_house_same_triplet_king_pair_beats_queen_pair() {

        when_compare(
                List.of(card(Suit.CLUB, Number.SEVEN), card(Suit.HEART, Number.SEVEN), card(Suit.DIAMOND, Number.SEVEN), card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING)),
                List.of(card(Suit.SPADE, Number.SEVEN), card(Suit.DIAMOND, Number.SEVEN), card(Suit.HEART, Number.SEVEN), card(Suit.DIAMOND, Number.QUEEN), card(Suit.SPADE, Number.QUEEN))
        );

        then_result_is(1);
    }

    @Test
    public void four_of_a_kind_beats_full_house() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.HEART, Number.TWO), card(Suit.DIAMOND, Number.TWO), card(Suit.SPADE, Number.TWO), card(Suit.CLUB, Number.THREE)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE), card(Suit.DIAMOND, Number.ACE), card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING))
        );

        then_result_is(1);
    }

    @Test
    public void four_of_a_kind_vs_four_of_a_kind_higher_quadruplet_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.SPADE, Number.KING), card(Suit.CLUB, Number.TWO)),
                List.of(card(Suit.CLUB, Number.QUEEN), card(Suit.HEART, Number.QUEEN), card(Suit.DIAMOND, Number.QUEEN), card(Suit.SPADE, Number.QUEEN), card(Suit.CLUB, Number.ACE))
        );

        then_result_is(1);
    }

    @Test
    public void four_of_a_kind_vs_four_of_a_kind_same_quadruplet_higher_kicker_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.SPADE, Number.KING), card(Suit.CLUB, Number.TWO)),
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.ACE))
        );

        then_result_is(-1);
    }

    @Test
    public void four_of_a_kind_vs_four_of_a_kind_same_quadruplet_same_kicker_number_is_tie() {

        when_compare(
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.SPADE, Number.KING), card(Suit.DIAMOND, Number.ACE)),
                List.of(card(Suit.CLUB, Number.KING), card(Suit.HEART, Number.KING), card(Suit.DIAMOND, Number.KING), card(Suit.HEART, Number.KING), card(Suit.CLUB, Number.ACE))
        );

        then_result_is(0);
    }

    @Test
    public void straight_flush_beats_four_of_a_kind() {

        when_compare(
                List.of(card(Suit.CLUB, Number.TWO), card(Suit.CLUB, Number.THREE), card(Suit.CLUB, Number.FOUR), card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SIX)),
                List.of(card(Suit.CLUB, Number.ACE), card(Suit.HEART, Number.ACE), card(Suit.DIAMOND, Number.ACE), card(Suit.SPADE, Number.ACE), card(Suit.CLUB, Number.KING))
        );

        then_result_is(1);
    }

    @Test
    public void straight_flush_vs_straight_flush_higher_highest_card_wins() {

        when_compare(
                List.of(card(Suit.CLUB, Number.FIVE), card(Suit.CLUB, Number.SIX), card(Suit.CLUB, Number.SEVEN), card(Suit.CLUB, Number.EIGHT), card(Suit.CLUB, Number.NINE)),
                List.of(card(Suit.HEART, Number.TWO), card(Suit.HEART, Number.THREE), card(Suit.HEART, Number.FOUR), card(Suit.HEART, Number.FIVE), card(Suit.HEART, Number.SIX))
        );

        then_result_is(1);
    }


}