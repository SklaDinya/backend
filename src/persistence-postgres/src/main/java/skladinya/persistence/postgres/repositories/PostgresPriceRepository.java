package skladinya.persistence.postgres.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.price.Price;
import skladinya.domain.repositories.PriceRepository;
import skladinya.persistence.postgres.entities.PriceEntity;
import skladinya.persistence.postgres.mappers.PriceMapper;

import java.util.List;
import java.util.UUID;

interface SpringPriceRepository extends JpaRepository<PriceEntity, UUID> {
    List<PriceEntity> findAllByStorageIdOrderByCreatedAtAsc(UUID storageId);
}

@Repository
@RequiredArgsConstructor
public class PostgresPriceRepository implements PriceRepository {

    private final SpringPriceRepository repo;

    @Override
    public Price create(Price price) {
        PriceEntity entity = PriceMapper.toEntity(price);
        PriceEntity saved = repo.save(entity);
        return PriceMapper.toDomain(saved);
    }

    @Override
    public List<Price> getAllByStorageId(UUID storageId) {
        return repo.findAllByStorageIdOrderByCreatedAtAsc(storageId).stream()
                .map(PriceMapper::toDomain)
                .toList();
    }
}
