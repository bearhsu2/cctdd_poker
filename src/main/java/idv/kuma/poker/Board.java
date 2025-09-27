package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {
    private final List<Card> cards;

    public static Board of(List<Card> cards) {
        return new Board(cards);
    }

    public List<Card> getCards() {
        return cards;
    }
}