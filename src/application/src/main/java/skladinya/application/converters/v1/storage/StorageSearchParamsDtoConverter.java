package skladinya.application.converters.v1.storage;

import skladinya.application.dtos.v1.storage.StorageSearchParamsDto;
import skladinya.domain.models.storage.StorageSearchOptions;
import skladinya.domain.models.storage.StorageStatus;

import java.util.List;

public final class StorageSearchParamsDtoConverter {

    private StorageSearchParamsDtoConverter() {
    }

    public static StorageSearchOptions toStoragesCoreEntity(StorageSearchParamsDto dto) {
        return toCoreEntity(dto, List.of(StorageStatus.Active));
    }

    public static StorageSearchOptions toRequestsCoreEntity(StorageSearchParamsDto dto) {
        return toCoreEntity(dto, List.of(StorageStatus.Created));
    }

    private static StorageSearchOptions toCoreEntity(StorageSearchParamsDto dto, List<StorageStatus> statuses) {
        return new StorageSearchOptions(
                dto.getName(),
                dto.getAddress(),
                statuses,
                dto.getPageSize() == null ? 50 : dto.getPageSize(),
                dto.getPageNumber() == null  ? 0 : dto.getPageNumber()
        );
    }
}
