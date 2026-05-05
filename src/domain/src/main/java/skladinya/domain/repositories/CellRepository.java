package skladinya.domain.repositories;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CellRepository {

    Cell create(Cell cell);

    Optional<Cell> getByName(UUID storageId, String name);

    List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options);

    List<Cell> getAllByCellIds(List<UUID> cellIds);

    List<String> getAllCellClasses(UUID storageId);
}
