package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettleTableServiceTest {

    @Test
    void shouldRetrieveTableSettleItAndSaveBack() {
        TableRepository tableRepository = new TableRepositoryInMemory();
        SettleTableService settleTableService = new SettleTableService(tableRepository);

        given_table("table-1", tableRepository);

        when_settle("table-1", settleTableService);

        then_table_status_should_be("table-1", TableStatus.SETTLED, 2, tableRepository);
    }

    private void given_table(String tableId, TableRepository tableRepository) {
        Table table = Table.create(tableId);
        tableRepository.save(table);
    }

    private void when_settle(String tableId, SettleTableService settleTableService) {
        settleTableService.settle(tableId);
    }

    private void then_table_status_should_be(String tableId, TableStatus expectedStatus, int expectedVersion, TableRepository tableRepository) {
        Table table = tableRepository.findById(tableId);
        assertThat(table.getStatus()).isEqualTo(expectedStatus);
        assertThat(table.getVersion()).isEqualTo(expectedVersion);
    }
}
