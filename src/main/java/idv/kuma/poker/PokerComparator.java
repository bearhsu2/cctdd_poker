package idv.kuma.poker;

public class PokerComparator {
    public int compare(CardNumber poker1, CardNumber poker2) {
        return Integer.compare(poker1.getNumber(), poker2.getNumber());
    }
}