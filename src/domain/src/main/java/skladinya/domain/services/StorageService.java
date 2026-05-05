package skladinya.domain.services;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageCreate;
import skladinya.domain.models.storage.StorageSearchOptions;
import skladinya.domain.models.storage.StorageUpdate;

import java.util.List;
import java.util.UUID;

public interface StorageService {

    void create(StorageCreate createForm);

    Storage getByStorageId(UUID storageId);

    List<Storage> getAllBySearchOptions(StorageSearchOptions options);

    Storage update(UUID storageId, StorageUpdate updateForm);

    Storage approve(UUID storageId);

    void reject(UUID storageId);
}
