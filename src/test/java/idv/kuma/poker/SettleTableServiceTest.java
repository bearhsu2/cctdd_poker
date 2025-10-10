package idv.kuma.poker;

import idv.kuma.poker.table.entity.Table;
import idv.kuma.poker.table.entity.TableStatus;
import idv.kuma.poker.table.adapter.TableRepositoryInMemory;
import idv.kuma.poker.table.usecase.SettleTableService;
import idv.kuma.poker.table.usecase.TableRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettleTableServiceTest {
    private final TableRepository tableRepository = new TableRepositoryInMemory();
    private final SettleTableService settleTableService = new SettleTableService(tableRepository);

    @Test
    void shouldRetrieveTableSettleItAndSaveBack() {
        given_table("table-1");

        when_settle("table-1");

        then_table_status_should_be("table-1", TableStatus.SETTLED, 2);
    }

    private void given_table(String tableId) {
        Table table = Table.create(tableId);
        tableRepository.save(table);
    }

    private void when_settle(String tableId) {
        settleTableService.settle(tableId);
    }

    private void then_table_status_should_be(String tableId, TableStatus expectedStatus, int expectedVersion) {
        Table table = tableRepository.findById(tableId);
        assertThat(table.getStatus()).isEqualTo(expectedStatus);
        assertThat(table.getVersion()).isEqualTo(expectedVersion);
    }
}
