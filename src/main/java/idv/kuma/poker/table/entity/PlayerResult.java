package idv.kuma.poker.table.entity;

public record PlayerResult(String userId, int rank) {

    public static PlayerResult of(String userId, int rank) {
        return new PlayerResult(userId, rank);
    }
}