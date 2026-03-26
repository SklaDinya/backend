package skladinya.domain.services;

import skladinya.domain.models.price.Price;

import java.util.List;
import java.util.UUID;

public interface PriceService {

    Price create(Price price);

    List<Price> getAllByStorageId(UUID storageId);
}
