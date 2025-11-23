package idv.kuma.poker.wallet.usecase;

import idv.kuma.poker.common.exception.EntityExistsException;
import idv.kuma.poker.wallet.entity.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AddBalanceService {
    private final WalletRepository walletRepository;

    @Transactional
    public void addBalance(String playerId, long amount) throws EntityExistsException {
        Wallet wallet = walletRepository.findByPlayerId(playerId);
        wallet.addBalance(amount);
        walletRepository.save(wallet);
    }
}