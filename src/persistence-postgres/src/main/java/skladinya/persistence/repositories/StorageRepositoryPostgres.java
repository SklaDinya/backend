package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.repositories.StorageRepository;
import skladinya.persistence.entities.StorageEntity;
import skladinya.persistence.mappers.StorageMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StorageRepositoryPostgres implements StorageRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Storage create(Storage storage) {
        if (storage == null) throw new IllegalArgumentException("Storage cannot be null");

        StorageEntity entity = StorageMapper.toEntity(storage);

        em.persist(entity);
        em.flush();

        return StorageMapper.toDomain(entity);
    }

    @Override
    public Optional<Storage> getByStorageId(UUID storageId) {
        if (storageId == null) return Optional.empty();

        StorageEntity entity = em.find(StorageEntity.class, storageId);
        return entity != null ? Optional.of(StorageMapper.toDomain(entity)) : Optional.empty();
    }

    @Override
    @Transactional
    public Storage update(UUID storageId, Storage storage) {
        if (storageId == null || storage == null) return null;

        StorageEntity entity = em.find(StorageEntity.class, storageId);
        if (entity == null) return null;

        entity.setName(storage.name());
        entity.setAddress(storage.address());
        entity.setDescription(storage.description());
        entity.setStatus(storage.status());
        entity.setCreatedAt(storage.createdAt());
        entity.setUpdatedAt(storage.updatedAt());

        em.merge(entity);
        em.flush();

        return StorageMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(UUID storageId) {
        if (storageId == null) return;

        StorageEntity entity = em.find(StorageEntity.class, storageId);
        if (entity != null) {
            em.remove(entity);
            em.flush();
        }
    }
}
