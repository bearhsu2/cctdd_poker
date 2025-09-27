package idv.kuma.poker;


import java.util.List;
import java.util.stream.Stream;

public class PlayerCards {
    private final List<Card> cards;

    private PlayerCards(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 2, "Player cards must be exactly 2 cards, but got " + cards.size());
        this.cards = cards;
    }

    public static PlayerCards of(List<Card> cards) {
        return new PlayerCards(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public Hand findBestHand(Board board) {
        List<Card> allCards = Stream.concat(cards.stream(), board.getCards().stream()).toList();

        return CombinationUtil.generateCombinations(allCards, 5)
            .stream()
            .map(Hand::of)
            .max(Hand::compareTo)
            .orElseThrow();
    }
}