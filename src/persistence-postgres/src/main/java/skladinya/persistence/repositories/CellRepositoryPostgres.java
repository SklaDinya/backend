package skladinya.persistence.repositories;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.repositories.CellRepository;

import java.util.List;
import java.util.UUID;

public class CellRepositoryPostgres implements CellRepository {
    @Override
    public Cell create(Cell cell) {
        return null;
    }

    @Override
    public List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options) {
        return List.of();
    }

    @Override
    public List<Cell> getAllByCellIds(List<UUID> cellIds) {
        return List.of();
    }

    @Override
    public List<String> getAllCellClasses(UUID storageId) {
        return List.of();
    }
}
