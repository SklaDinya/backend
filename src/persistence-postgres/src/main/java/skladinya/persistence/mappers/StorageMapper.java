package skladinya.persistence.mappers;

import skladinya.domain.models.storage.Storage;
import skladinya.persistence.entities.StorageEntity;

public class StorageMapper {

    public static StorageEntity toEntity(Storage storage) {
        if (storage == null) {
            return null;
        }

        return new StorageEntity(
                storage.storageId(),
                storage.name(),
                storage.address(),
                storage.description(),
                storage.status(),
                storage.createdAt(),
                storage.updatedAt()
        );
    }

    public static Storage toDomain(StorageEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Storage(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
