package idv.kuma.poker.wallet.usecase;

import idv.kuma.poker.wallet.entity.Wallet;

public interface WalletRepository {
    Wallet findByPlayerId(String playerId);
    void save(Wallet wallet);
}