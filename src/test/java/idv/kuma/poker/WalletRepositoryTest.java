package idv.kuma.poker;

import idv.kuma.poker.common.OptimisticLockException;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void save_should_throw_exception_when_version_conflict_occurs() {
        Wallet wallet = Wallet.create("user-1", 1000);
        walletRepository.save(wallet);

        Wallet walletTx1 = walletRepository.findByPlayerId("user-1");
        Wallet walletTx2 = walletRepository.findByPlayerId("user-1");

        walletTx1.addBalance(100);
        walletRepository.save(walletTx1);

        walletTx2.addBalance(200);

        assertThrows(OptimisticLockException.class, () -> {
            walletRepository.save(walletTx2);
        });
    }
}