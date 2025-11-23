package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.tool.CombinationUtil;
import idv.kuma.poker.common.tool.DBCUtil;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

public class HoleCards {
    @Getter
    private final List<Card> cards;

    private HoleCards(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 2, "Player cards must be exactly 2 cards, but got " + cards.size());
        this.cards = cards;
    }

    public static HoleCards of(List<Card> cards) {
        return new HoleCards(cards);
    }


    public PokerHand findBestHand(Board board) {
        List<Card> allCards = Stream.concat(cards.stream(), board.getCards().stream()).toList();

        return CombinationUtil.generateCombinations(allCards, PokerHand.HAND_SIZE)
            .stream()
            .map(PokerHand::of)
            .max(PokerHand::compareTo)
            .orElseThrow();
    }
}