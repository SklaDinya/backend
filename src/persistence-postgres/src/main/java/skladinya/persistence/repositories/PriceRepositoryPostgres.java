package skladinya.persistence.repositories;

import skladinya.domain.models.price.Price;
import skladinya.domain.repositories.PriceRepository;

import java.util.List;
import java.util.UUID;

public class PriceRepositoryPostgres implements PriceRepository {
    @Override
    public Price create(Price price) {
        return null;
    }

    @Override
    public List<Price> getAllByStorageId(UUID storageId) {
        return List.of();
    }
}
