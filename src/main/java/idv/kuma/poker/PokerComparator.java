package idv.kuma.poker;


public class PokerComparator {

    public Hand findMaxCategory(PlayerCards playerCards, Board board) {
        return playerCards.findBestHand(board);
    }

}