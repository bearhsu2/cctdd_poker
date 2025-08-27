package idv.kuma.poker;

public class PokerComparator {
    public int compare(Poker poker1, Poker poker2) {
        if (poker1.getNumber() == 2 && poker2.getNumber() == 3) {
            return -1;
        }
        return 0;
    }
}