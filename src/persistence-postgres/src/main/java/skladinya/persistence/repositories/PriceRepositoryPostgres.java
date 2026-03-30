package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.price.Price;
import skladinya.domain.repositories.PriceRepository;
import skladinya.persistence.entities.PriceEntity;
import skladinya.persistence.entities.StorageEntity;
import skladinya.persistence.mappers.PriceMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PriceRepositoryPostgres implements PriceRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Price create(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        StorageEntity storage = em.find(StorageEntity.class, price.storageId());

        PriceEntity entity = PriceMapper.toEntity(price, storage);

        em.persist(entity);
        em.flush();

        return PriceMapper.toDomain(entity);
    }

    @Override
    public List<Price> getAllByStorageId(UUID storageId) {
        if (storageId == null) return List.of();

        var query = em.createQuery(
                "SELECT p FROM PriceEntity p WHERE p.storage.id = :storageId ORDER BY p.cellClass ASC",
                PriceEntity.class
        );
        query.setParameter("storageId", storageId);

        return query.getResultStream()
                .map(PriceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
