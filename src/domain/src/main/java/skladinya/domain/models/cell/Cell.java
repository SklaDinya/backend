package skladinya.domain.models.cell;

import java.time.LocalDateTime;
import java.util.UUID;

public record Cell(
        UUID cellId,
        UUID storageId,
        String name,
        String cellClass,
        LocalDateTime createdAt
) {

    public Cell(UUID storageId, String name, String cellClass) {
        this(
                UUID.randomUUID(),
                storageId,
                name,
                cellClass,
                LocalDateTime.now()
        );
    }
}
