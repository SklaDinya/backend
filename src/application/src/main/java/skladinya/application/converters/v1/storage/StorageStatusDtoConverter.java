package skladinya.application.converters.v1.storage;

import skladinya.application.dtos.v1.storage.StorageStatusDto;
import skladinya.domain.models.storage.StorageStatus;

public final class StorageStatusDtoConverter {

    private StorageStatusDtoConverter() {
    }

    public static StorageStatusDto toDto(StorageStatus status) {
        return switch (status) {
            case StorageStatus.Created -> StorageStatusDto.Created;
            case StorageStatus.Active -> StorageStatusDto.Active;
        };
    }
}
