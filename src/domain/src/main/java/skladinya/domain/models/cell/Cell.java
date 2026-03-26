package skladinya.domain.models.cell;

import java.util.Date;
import java.util.UUID;

public record Cell(
        UUID cellId,
        UUID storageId,
        String name,
        String cellClass,
        Date createdAt
) {

    public Cell(UUID storageId, String name, String cellClass) {
        this(
                UUID.randomUUID(),
                storageId,
                name,
                cellClass,
                new Date()
        );
    }
}
