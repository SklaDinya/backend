package skladinya.persistence.repositories;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.repositories.StorageRepository;

import java.util.Optional;
import java.util.UUID;

public class StorageRepositoryPostgres implements StorageRepository {
    @Override
    public Storage create(Storage storage) {
        return null;
    }

    @Override
    public Optional<Storage> getByStorageId(UUID storageId) {
        return Optional.empty();
    }

    @Override
    public Storage update(UUID storageId, Storage storage) {
        return null;
    }

    @Override
    public void delete(UUID storageId) {

    }
}
