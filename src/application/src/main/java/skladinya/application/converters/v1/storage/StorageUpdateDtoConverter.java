package skladinya.application.converters.v1.storage;

import skladinya.application.dtos.v1.storage.StorageUpdateDto;
import skladinya.domain.models.storage.StorageUpdate;

public final class StorageUpdateDtoConverter {

    private StorageUpdateDtoConverter() {
    }

    public static StorageUpdate toCoreEntity(StorageUpdateDto dto) {
        return new StorageUpdate(
                dto.getName(),
                dto.getAddress(),
                dto.getDescription()
        );
    }
}
