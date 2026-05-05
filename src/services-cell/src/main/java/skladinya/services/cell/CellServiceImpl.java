package skladinya.services.cell;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellCreate;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.repositories.CellRepository;
import skladinya.domain.services.CellService;
import skladinya.domain.services.StorageService;

import java.util.List;
import java.util.UUID;

@Service
@RequestScope
public class CellServiceImpl implements CellService {

    private final CellRepository cellRepository;

    private final StorageService storageService;

    private final Synchronizer synchronizer;

    public CellServiceImpl(
            CellRepository cellRepository,
            StorageService storageService,
            Synchronizer synchronizer
    ) {
        this.cellRepository = cellRepository;
        this.storageService = storageService;
        this.synchronizer = synchronizer;
    }

    @Override
    public Cell create(UUID storageId, CellCreate createForm) {
        return synchronizer.executeTransactionFunction(() -> {
            storageService.getByStorageId(storageId);
            var found = cellRepository.getByName(storageId, createForm.name()).orElse(null);
            if (found != null) {
                throw SklaDinyaException.conflict(
                        String.format("Cell with name %s already exists", createForm.name()));
            }
            var cell = new Cell(storageId, createForm.name(), createForm.cellClass());
            return cellRepository.create(cell);
        });
    }

    @Override
    public List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options) {
        return synchronizer.executeSingleFunction(() -> {
            storageService.getByStorageId(storageId);
            return cellRepository.getAllBySearchOptions(storageId, options);
        });
    }

    @Override
    public List<Cell> getAllByCellIds(UUID storageId, List<UUID> cellIds) {
        return synchronizer.executeSingleFunction(() -> {
            storageService.getByStorageId(storageId);
            return cellRepository.getAllByCellIds(cellIds).stream()
                    .filter(c -> c.storageId().equals(storageId)).toList();
        });
    }

    @Override
    public List<String> getAllCellClasses(UUID storageId) {
        return synchronizer.executeSingleFunction(() -> {
            storageService.getByStorageId(storageId);
            return cellRepository.getAllCellClasses(storageId);
        });
    }
}
