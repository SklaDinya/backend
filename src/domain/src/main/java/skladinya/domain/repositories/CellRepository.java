package skladinya.domain.repositories;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;

import java.util.UUID;

public interface CellRepository {

    Cell create(Cell cell);

    Iterable<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options);

    Iterable<String> getAllCellClasses(UUID storageId);
}
