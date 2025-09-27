package idv.kuma.poker;


import java.util.List;

public class Board {
    private final List<Card> cards;

    private Board(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Board must be exactly 5 cards, but got " + cards.size());
        this.cards = cards;
    }

    public static Board of(List<Card> cards) {
        return new Board(cards);
    }

    public List<Card> getCards() {
        return cards;
    }
}