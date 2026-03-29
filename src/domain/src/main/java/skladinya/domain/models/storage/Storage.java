package skladinya.domain.models.storage;

import java.time.LocalDateTime;
import java.util.UUID;

public record Storage(
        UUID storageId,
        String name,
        String address,
        String description,
        StorageStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public Storage(
            UUID storageId,
            String name,
            String address,
            String description,
            StorageStatus status,
            LocalDateTime createdAt
    ) {
        this(
                storageId,
                name,
                address,
                description,
                status,
                createdAt,
                LocalDateTime.now()
        );
    }

    public Storage(
            String name,
            String address,
            String description
    ) {
        this(
                UUID.randomUUID(),
                name,
                address,
                description,
                StorageStatus.Created,
                LocalDateTime.now()
        );
    }
}
