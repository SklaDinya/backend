package skladinya.domain.models.storage;

import java.util.UUID;

public record Storage(
        UUID storageId,
        String name,
        String address,
        String description,
        StorageStatus status
) {
}
