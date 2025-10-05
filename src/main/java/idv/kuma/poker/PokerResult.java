package idv.kuma.poker;

public class PokerResult {

    public int getRank(int position) {
        if (position == 0) {
            return 1;
        }
        return 2;
    }
}
