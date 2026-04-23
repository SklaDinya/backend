package skladinya.persistence.postgres.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.repositories.StorageRepository;
import skladinya.persistence.postgres.entities.StorageEntity;
import skladinya.persistence.postgres.mappers.StorageMapper;
import skladinya.persistence.postgres.mappers.enums.StorageStatusMapper;

import java.util.Optional;
import java.util.UUID;

interface SpringStorageRepository extends JpaRepository<StorageEntity, UUID> {}

@Repository
@RequiredArgsConstructor
public class PostgresStorageRepository implements StorageRepository {

    private final SpringStorageRepository repo;

    @Override
    public Storage create(Storage storage) {
        StorageEntity entity = StorageMapper.toEntity(storage);
        StorageEntity saved = repo.save(entity);
        return StorageMapper.toDomain(saved);
    }

    @Override
    public Optional<Storage> getByStorageId(UUID storageId) {
        return repo.findById(storageId)
                .map(StorageMapper::toDomain);
    }

    @Override
    public Storage update(UUID storageId, Storage storage) {
        return repo.findById(storageId)
                .map(existing -> {
                    existing.setName(storage.name());
                    existing.setAddress(storage.address());
                    existing.setDescription(storage.description());
                    existing.setStatus(StorageStatusMapper.toEntity(storage.status()));
                    existing.setUpdatedAt(storage.updatedAt());

                    return StorageMapper.toDomain(repo.save(existing));
                })
                .orElse(null);
    }

    @Override
    public void delete(UUID storageId) {
        repo.deleteById(storageId);
    }
}
