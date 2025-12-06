package idv.kuma.poker.table.entity;

public record PlayerResult(int position, String userId, int rank) {

    public static PlayerResult of(int position, String userId, int rank) {
        return new PlayerResult(position, userId, rank);
    }
}