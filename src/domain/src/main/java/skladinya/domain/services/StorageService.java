package skladinya.domain.services;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageCreate;
import skladinya.domain.models.storage.StorageUpdate;

import java.util.UUID;

public interface StorageService {

    void create(StorageCreate createForm);

    Storage getByStorageId(UUID storageId);

    Storage update(UUID storageId, StorageUpdate updateForm);

    Storage approve(UUID storageId);

    void reject(UUID storageId);
}
