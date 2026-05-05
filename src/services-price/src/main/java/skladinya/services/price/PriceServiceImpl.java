package skladinya.services.price;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.price.Price;
import skladinya.domain.models.price.PriceCreate;
import skladinya.domain.repositories.PriceRepository;
import skladinya.domain.services.PriceService;
import skladinya.domain.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequestScope
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final StorageService storageService;
    private final Synchronizer synchronizer;

    public PriceServiceImpl(
            PriceRepository priceRepository,
            StorageService storageService,
            Synchronizer synchronizer
    ) {
        this.priceRepository = priceRepository;
        this.storageService = storageService;
        this.synchronizer = synchronizer;
    }

    @Override
    public Price create(UUID storageId, PriceCreate createForm) {
        return synchronizer.executeTransactionFunction(() -> {

            try {
                storageService.getByStorageId(storageId);
            } catch (SklaDinyaException ex) {
                throw SklaDinyaException.notFound("Storage not found");
            }

            Price price = new Price(
                    UUID.randomUUID(),
                    storageId,
                    createForm.cellClass(),
                    createForm.price(),
                    LocalDateTime.now()
            );

            return priceRepository.create(price);
        });
    }

    @Override
    public List<Price> getAllByStorageId(UUID storageId) {
        return synchronizer.executeTransactionFunction(() -> {
            storageService.getByStorageId(storageId);
            return priceRepository.getAllByStorageId(storageId)
                    .stream()
                    .collect(Collectors.toMap(
                            Price::cellClass,
                            p -> p,
                            (firstPrice, lastPrice) -> lastPrice))
                    .values()
                    .stream()
                    .toList();
        });
    }

}
