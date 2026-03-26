package skladinya.domain.repositories;

import skladinya.domain.models.price.Price;

import java.util.UUID;

public interface PriceRepository {

    Price create(Price price);

    Iterable<Price> getAllByStorageId(UUID storageId);
}
