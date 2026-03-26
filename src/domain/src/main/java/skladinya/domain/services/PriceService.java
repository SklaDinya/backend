package skladinya.domain.services;

import skladinya.domain.models.price.Price;
import skladinya.domain.models.price.PriceCreate;

import java.util.List;
import java.util.UUID;

public interface PriceService {

    Price create(UUID storageId, PriceCreate createForm);

    List<Price> getAllByStorageId(UUID storageId);
}
