package idv.kuma.poker;

import idv.kuma.poker.common.OptimisticLockException;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

    @Test
    void save_should_throw_exception_when_two_threads_try_to_insert_same_wallet_concurrently() throws Exception {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(2);
        AtomicReference<Exception> exception1 = new AtomicReference<>();
        AtomicReference<Exception> exception2 = new AtomicReference<>();

        Thread thread1 = new Thread(() -> {
            try {
                startLatch.await();
                Wallet wallet1 = Wallet.create("user-3", 1000);
                walletRepository.save(wallet1);
            } catch (Exception e) {
                exception1.set(e);
            } finally {
                endLatch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                startLatch.await();
                Wallet wallet2 = Wallet.create("user-3", 2000);
                walletRepository.save(wallet2);
            } catch (Exception e) {
                exception2.set(e);
            } finally {
                endLatch.countDown();
            }
        });

        thread1.start();
        thread2.start();
        startLatch.countDown();
        endLatch.await();

        boolean oneSucceededOneThrew = (exception1.get() == null && exception2.get() != null) ||
                (exception1.get() != null && exception2.get() == null);
        assertThat(oneSucceededOneThrew).isTrue();

        Exception thrownException = exception1.get() != null ? exception1.get() : exception2.get();
        assertThat(thrownException)
            .satisfiesAnyOf(
                e -> assertThat(e).isInstanceOf(DuplicateKeyException.class),
                e -> assertThat(e).isInstanceOf(OptimisticLockException.class)
            );
    }

    @Test
    void save_should_throw_optimistic_lock_exception_when_inserting_after_another_insert_completes() {
        Wallet wallet1 = Wallet.create("user-4", 1000);
        walletRepository.save(wallet1);

        Wallet wallet2 = Wallet.create("user-4", 2000);

        assertThrows(OptimisticLockException.class, () -> {
            walletRepository.save(wallet2);
        });
    }
}