package idv.kuma.poker.wallet.usecase;

import idv.kuma.poker.common.exception.EntityExistsException;
import idv.kuma.poker.common.exception.EntityVersionConflictException;
import idv.kuma.poker.wallet.entity.Wallet;

public interface WalletRepository {
    Wallet findByPlayerId(String playerId);
    void insert(Wallet wallet) throws EntityExistsException;
    void update(Wallet wallet) throws EntityVersionConflictException;
}