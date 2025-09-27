package idv.kuma.poker;


import java.util.List;

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
}