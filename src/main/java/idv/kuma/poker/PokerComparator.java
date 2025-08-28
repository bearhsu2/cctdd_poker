package idv.kuma.poker;

public class PokerComparator {
    public int compare(Poker poker1, Poker poker2) {
        return Integer.compare(poker1.getNumber(), poker2.getNumber());
    }
}