package skladinya.persistence.mappers.enums;

import skladinya.domain.models.storage.StorageStatus;
import skladinya.persistence.entities.enums.StorageStatusEntity;

public class StorageStatusMapper {

    public static StorageStatusEntity toEntity(StorageStatus domain) {
        return domain != null ? StorageStatusEntity.valueOf(domain.name()) : null;
    }

    public static StorageStatus toDomain(StorageStatusEntity entity) {
        return entity != null ? StorageStatus.valueOf(entity.name()) : null;
    }
}
