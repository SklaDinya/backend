package skladinya.persistence.postgres.repositories;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageSearchOptions;
import skladinya.domain.repositories.StorageRepository;
import skladinya.persistence.postgres.entities.StorageEntity;
import skladinya.persistence.postgres.mappers.StorageMapper;
import skladinya.persistence.postgres.mappers.enums.StorageStatusMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringStorageRepository extends JpaRepository<StorageEntity, UUID>, JpaSpecificationExecutor<StorageEntity> {
}

class StorageSpecification {

    public static Specification<StorageEntity> byOptions(StorageSearchOptions options) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (options.name() != null) {
                predicates.add(cb.like(root.get("name"), "%" + options.name() + "%"));
            }

            if (options.address() != null) {
                predicates.add(cb.like(root.get("address"), "%" + options.address() + "%"));
            }

            if (!options.statuses().isEmpty()) {
                var statuses = options.statuses().stream().map(StorageStatusMapper::toEntity).toList();
                predicates.add(root.get("status").in(statuses));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}

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
    public List<Storage> getAllBySearchOptions(StorageSearchOptions options) {
        var pageable = PageRequest.of(options.pageNumber(), options.pageSize());

        return repo.findAll(StorageSpecification.byOptions(options), pageable)
                .stream()
                .map(StorageMapper::toDomain)
                .toList();
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
