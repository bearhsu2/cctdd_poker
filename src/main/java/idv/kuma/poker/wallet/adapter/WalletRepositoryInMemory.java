package idv.kuma.poker.wallet.adapter;

import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;

import java.util.HashMap;
import java.util.Map;

public class WalletRepositoryInMemory implements WalletRepository {
    private final Map<String, Wallet> wallets = new HashMap<>();


    @Override
    public Wallet findByPlayerId(String playerId) {
        return wallets.get(playerId);
    }

    @Override
    public void save(Wallet wallet) {
        wallets.put(wallet.getPlayerId(), wallet);
    }
}