package skladinya.application.converters.v1.storage;

import skladinya.application.dtos.v1.storage.StorageCreateDto;
import skladinya.domain.models.operator.OperatorCreate;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.storage.StorageCreate;

public final class StorageCreateDtoConverter {

    private StorageCreateDtoConverter() {
    }

    public static StorageCreate toCoreEntity(StorageCreateDto dto) {
        var operatorCreate = new OperatorCreate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                OperatorRole.MainOperator
        );
        return new StorageCreate(
                operatorCreate,
                dto.getStorageName(),
                dto.getAddress(),
                dto.getDescription()
        );
    }
}
