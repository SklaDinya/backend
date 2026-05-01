package skladinya.domain.repositories;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageSearchOptions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageRepository {

    Storage create(Storage storage);

    Optional<Storage> getByStorageId(UUID storageId);

    List<Storage> getAllBySearchOptions(StorageSearchOptions options);

    Storage update(UUID storageId, Storage storage);

    void delete(UUID storageId);
}
