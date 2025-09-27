package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayCards {
    private final List<Card> cards;

    public static PlayCards of(List<Card> cards) {
        return new PlayCards(cards);
    }

    public List<Card> getCards() {
        return cards;
    }
}