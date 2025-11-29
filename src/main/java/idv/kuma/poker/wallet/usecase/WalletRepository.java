package idv.kuma.poker.wallet.usecase;

import idv.kuma.poker.common.exception.EntityExistsException;
import idv.kuma.poker.common.exception.EntityVersionConflictException;
import idv.kuma.poker.common.exception.PersistenceException;
import idv.kuma.poker.wallet.entity.Wallet;

public interface WalletRepository {
    Wallet findByPlayerId(String playerId) throws PersistenceException;
    void insert(Wallet wallet) throws EntityExistsException, PersistenceException;
    void update(Wallet wallet) throws EntityVersionConflictException, PersistenceException;
}