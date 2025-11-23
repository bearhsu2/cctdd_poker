package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.tool.DBCUtil;
import lombok.Getter;

import java.util.List;

public class Board {
    @Getter
    private final List<Card> cards;

    private Board(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Board must be exactly 5 cards, but got " + cards.size());
        this.cards = cards;
    }

    public static Board of(List<Card> cards) {
        return new Board(cards);
    }

}