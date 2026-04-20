package skladinya.tests.helper.builder;

import skladinya.domain.models.cell.Cell;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CellBuilder {

    private UUID cellId = UUID.randomUUID();
    private UUID storageId = UUID.randomUUID();
    private String name = UUID.randomUUID().toString();
    private String cellClass = UUID.randomUUID().toString();
    private LocalDateTime createdAt = LocalDateTime.now();

    private CellBuilder() {
    }

    public static CellBuilder builder() {
        return new CellBuilder();
    }

    public CellBuilder cellId(UUID cellId) {
        this.cellId = cellId;
        return this;
    }

    public CellBuilder storageId(UUID storageId) {
        this.storageId = storageId;
        return this;
    }

    public CellBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CellBuilder cellClass(String cellClass) {
        this.cellClass = cellClass;
        return this;
    }

    public CellBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Cell build() {
        return new Cell(cellId, storageId, name, cellClass, createdAt);
    }
}
