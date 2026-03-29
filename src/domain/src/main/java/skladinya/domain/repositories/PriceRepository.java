package skladinya.domain.repositories;

import skladinya.domain.models.price.Price;

import java.util.List;
import java.util.UUID;

public interface PriceRepository {

    Price create(Price price);

    List<Price> getAllByStorageId(UUID storageId);
}
