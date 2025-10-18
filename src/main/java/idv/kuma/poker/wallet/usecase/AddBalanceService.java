package idv.kuma.poker.wallet.usecase;

import idv.kuma.poker.wallet.entity.Wallet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddBalanceService {
    private final WalletRepository walletRepository;

    public void addBalance(String playerId, long amount) {
        Wallet wallet = walletRepository.findByPlayerId(playerId);
        wallet.addBalance(amount);
        walletRepository.save(wallet);
    }
}