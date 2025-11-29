package idv.kuma.poker.wallet.adapter;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.common.exception.PersistenceException;
import idv.kuma.poker.generated.QWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletRepositoryQueryDslTest {

    @Mock
    private SQLQueryFactory queryFactory;

    @Mock
    private SQLQuery<Object> sqlQuery;

    private WalletRepositoryQueryDsl walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository = new WalletRepositoryQueryDsl(queryFactory);
    }

    @Test
    void find_by_player_id_should_throw_persistence_exception_when_database_error_occurs() {
        when(queryFactory.select(any(Expression.class))).thenReturn(sqlQuery);
        when(sqlQuery.from(any(QWallet.class))).thenReturn(sqlQuery);
        when(sqlQuery.where(any(Predicate.class))).thenReturn(sqlQuery);
        when(sqlQuery.fetchOne()).thenThrow(new RuntimeException("Database connection lost"));

        assertThrows(PersistenceException.class, () -> {
            walletRepository.findByPlayerId("player-1");
        });
    }
}