package skladinya.domain.repositories;

import skladinya.domain.models.storage.Storage;

import java.util.Optional;
import java.util.UUID;

public interface StorageRepository {

    Storage create(Storage storage);

    Optional<Storage> getByStorageId(UUID storageId);

    Storage update(UUID storageId, Storage storage);

    void delete(UUID storageId);
}
