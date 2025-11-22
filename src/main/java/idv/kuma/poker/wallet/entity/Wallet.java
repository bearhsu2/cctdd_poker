package idv.kuma.poker.wallet.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wallet {
    private final String playerId;
    private int version;
    private long balance;

    public static Wallet create(String playerId, long initialBalance) {
        return new Wallet(playerId, 1, initialBalance);
    }

    public static Wallet restore(String playerId, int version, long balance) {
        return new Wallet(playerId, version, balance);
    }

    public void addBalance(long amount) {
        this.balance += amount;
        this.version++;
    }
}