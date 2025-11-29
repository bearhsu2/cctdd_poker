package idv.kuma.poker.wallet.adapter;

import com.querydsl.core.types.Expression;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.common.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletRepositoryQueryDslIsolatedTest {

    @Mock
    private SQLQueryFactory queryFactory;

    private WalletRepositoryQueryDsl walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository = new WalletRepositoryQueryDsl(queryFactory);
    }

    @Test
    void find_by_player_id_should_throw_persistence_exception_when_database_error_occurs() {
        when(queryFactory.select(any(Expression.class)))
                .thenThrow(new RuntimeException("any runtime exception"));

        assertThrows(PersistenceException.class, () -> {
            walletRepository.findByPlayerId("player-1");
        });
    }
}