package skladinya.domain.services;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellCreate;
import skladinya.domain.models.cell.CellSearchOptions;

import java.util.List;
import java.util.UUID;

public interface CellService {

    Cell create(UUID storageId, CellCreate createForm);

    List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options);

    List<Cell> getAllByCellIds(List<UUID> cellIds);

    List<String> getClasses(UUID storageId);
}
