package idv.kuma.poker;


import java.util.List;

public class PlayCards {
    private final List<Card> cards;

    private PlayCards(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 2, "Player cards must be exactly 2 cards, but got " + cards.size());
        this.cards = cards;
    }

    public static PlayCards of(List<Card> cards) {
        return new PlayCards(cards);
    }

    public List<Card> getCards() {
        return cards;
    }
}