package skladinya.application.converters.v1.storage;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.storage.StorageGetDto;
import skladinya.domain.models.storage.Storage;

public final class StorageGetDtoConverter {

    private StorageGetDtoConverter() {
    }

    public static StorageGetDto toDto(Storage storage) {
        return new StorageGetDto(
                storage.storageId(),
                storage.name(),
                storage.address(),
                storage.description(),
                StorageStatusDtoConverter.toDto(storage.status()),
                TimeConverter.toOffsetDateTime(storage.createdAt()),
                TimeConverter.toOffsetDateTime(storage.updatedAt())
        );
    }
}
