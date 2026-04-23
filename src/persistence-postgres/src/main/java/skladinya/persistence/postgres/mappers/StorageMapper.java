package skladinya.persistence.postgres.mappers;

import skladinya.domain.models.storage.Storage;
import skladinya.persistence.postgres.entities.StorageEntity;
import skladinya.persistence.postgres.mappers.enums.StorageStatusMapper;

public class StorageMapper {

    public static StorageEntity toEntity(Storage storage) {
        StorageEntity entity = storage != null ? new StorageEntity() : null;

        if (entity != null) {
            entity.setId(storage.storageId());
            entity.setName(storage.name());
            entity.setAddress(storage.address());
            entity.setDescription(storage.description());
            entity.setStatus(StorageStatusMapper.toEntity(storage.status()));
            entity.setCreatedAt(storage.createdAt());
            entity.setUpdatedAt(storage.updatedAt());
        }

        return entity;
    }

    public static Storage toDomain(StorageEntity entity) {
        return entity != null
                ? new Storage(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getDescription(),
                StorageStatusMapper.toDomain(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        )
                : null;
    }
}
