package skladinya.domain.models.storage;

import java.util.Date;
import java.util.UUID;

public record Storage(
        UUID storageId,
        String name,
        String address,
        String description,
        StorageStatus status,
        Date createdAt,
        Date updatedAt
) {

    public Storage(
            UUID storageId,
            String name,
            String address,
            String description,
            StorageStatus status,
            Date createdAt
    ) {
        this(
                storageId,
                name,
                address,
                description,
                status,
                createdAt,
                new Date()
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
                new Date()
        );
    }
}
