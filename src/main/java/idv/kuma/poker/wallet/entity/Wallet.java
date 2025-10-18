package idv.kuma.poker.wallet.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wallet {
    private final String id;
    private final String playerId;
    private int version;
    private long balance;

    public static Wallet restore(String id, String playerId, int version, long balance) {
        return new Wallet(id, playerId, version, balance);
    }

    public void addBalance(long amount) {
        this.balance += amount;
        this.version++;
    }
}