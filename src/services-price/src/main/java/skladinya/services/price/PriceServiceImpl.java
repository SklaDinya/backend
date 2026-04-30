package skladinya.services.price;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.models.price.Price;
import skladinya.domain.models.price.PriceCreate;
import skladinya.domain.services.PriceService;

import java.util.List;
import java.util.UUID;

@Service
@RequestScope
public class PriceServiceImpl implements PriceService {
    @Override
    public Price create(UUID storageId, PriceCreate createForm) {
        return null;
    }

    @Override
    public List<Price> getAllByStorageId(UUID storageId) {
        return List.of();
    }
}
