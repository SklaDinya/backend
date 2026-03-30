package skladinya.persistence.mappers;

import skladinya.domain.models.price.Price;
import skladinya.persistence.entities.PriceEntity;
import skladinya.persistence.entities.StorageEntity;

import java.util.UUID;

public class PriceMapper {

    public static PriceEntity toEntity(Price price, StorageEntity storage) {
        if (price == null) {
            return null;
        }

        return new PriceEntity(
                price.priceId(),
                price.cellClass(),
                price.price(),
                price.createdAt(),
                storage
        );
    }

    public static Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        UUID storageId = entity.getStorage() != null
                ? entity.getStorage().getId()
                : null;

        return new Price(
                entity.getId(),
                storageId,
                entity.getCellClass(),
                entity.getPrice(),
                entity.getCreatedAt()
        );
    }
}
