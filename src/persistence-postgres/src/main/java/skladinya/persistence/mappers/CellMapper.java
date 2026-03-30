package skladinya.persistence.mappers;

import skladinya.domain.models.cell.Cell;
import skladinya.persistence.entities.CellEntity;
import skladinya.persistence.entities.StorageEntity;

import java.util.UUID;

public class CellMapper {

    public static CellEntity toEntity(Cell cell, StorageEntity storage) {
        if (cell == null) {
            return null;
        }

        return new CellEntity(
                cell.cellId(),
                cell.name(),
                cell.cellClass(),
                cell.createdAt(),
                storage
        );
    }

    public static Cell toDomain(CellEntity entity) {
        if (entity == null) {
            return null;
        }

        UUID storageId = entity.getStorage() != null
                ? entity.getStorage().getId()
                : null;

        return new Cell(
                entity.getId(),
                storageId,
                entity.getName(),
                entity.getCellClass(),
                entity.getCreatedAt()
        );
    }
}
