package idv.kuma.poker.wallet.adapter;

import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.common.OptimisticLockException;
import idv.kuma.poker.generated.QWallet;
import idv.kuma.poker.generated.WalletDbDto;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WalletRepositoryQueryDsl implements WalletRepository {
    private final SQLQueryFactory queryFactory;
    private static final QWallet qWallet = QWallet.wallet;

    @Override
    @Transactional(readOnly = true)
    public Wallet findByPlayerId(String playerId) {
        WalletDbDto dto = queryFactory
            .select(Projections.bean(WalletDbDto.class,
                qWallet.playerId,
                qWallet.balance,
                qWallet.version))
            .from(qWallet)
            .where(qWallet.playerId.eq(playerId))
            .fetchOne();

        return dto == null ? null : toEntity(dto);
    }

    @Override
    @Transactional
    public void save(Wallet wallet) {
        Long count = queryFactory
            .select(qWallet.playerId.count())
            .from(qWallet)
            .where(qWallet.playerId.eq(wallet.getPlayerId()))
            .fetchOne();

        if (count == 0) {
            queryFactory.insert(qWallet)
                .set(qWallet.playerId, wallet.getPlayerId())
                .set(qWallet.balance, wallet.getBalance())
                .set(qWallet.version, wallet.getVersion())
                .execute();
        } else {
            long rowsUpdated = queryFactory.update(qWallet)
                .set(qWallet.balance, wallet.getBalance())
                .set(qWallet.version, wallet.getVersion())
                .where(qWallet.playerId.eq(wallet.getPlayerId())
                    .and(qWallet.version.eq(wallet.getVersion() - 1)))
                .execute();

            if (rowsUpdated == 0) {
                throw new OptimisticLockException("Wallet " + wallet.getPlayerId() + " has been modified by another transaction");
            }
        }
    }

    private Wallet toEntity(WalletDbDto dto) {
        return Wallet.restore(dto.getPlayerId(), dto.getVersion(), dto.getBalance());
    }
}