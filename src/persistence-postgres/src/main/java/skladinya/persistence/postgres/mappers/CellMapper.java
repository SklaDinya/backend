package skladinya.persistence.postgres.mappers;

import skladinya.domain.models.cell.Cell;
import skladinya.persistence.postgres.entities.CellEntity;

public class CellMapper {

    public static CellEntity toEntity(Cell cell) {
        CellEntity entity = cell != null ? new CellEntity() : null;

        if (entity != null) {
            entity.setId(cell.cellId());
            entity.setName(cell.name());
            entity.setCellClass(cell.cellClass());
            entity.setCreatedAt(cell.createdAt());
            entity.setStorageId(cell.storageId());
        }

        return entity;
    }

    public static Cell toDomain(CellEntity entity) {
        return entity != null
                ? new Cell(
                entity.getId(),
                entity.getStorageId(),
                entity.getName(),
                entity.getCellClass(),
                entity.getCreatedAt()
        )
                : null;
    }
}
