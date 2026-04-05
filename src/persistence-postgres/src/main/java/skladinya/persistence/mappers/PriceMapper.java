package skladinya.persistence.mappers;

import skladinya.domain.models.price.Price;
import skladinya.persistence.entities.PriceEntity;

public class PriceMapper {

    public static PriceEntity toEntity(Price price) {
        PriceEntity entity = price != null ? new PriceEntity() : null;

        if (entity != null) {
            entity.setId(price.priceId());
            entity.setCellClass(price.cellClass());
            entity.setPrice(price.price());
            entity.setCreatedAt(price.createdAt());
            entity.setStorageId(price.storageId());
        }

        return entity;
    }

    public static Price toDomain(PriceEntity entity) {
        return entity != null
                ? new Price(
                entity.getId(),
                entity.getStorageId(),
                entity.getCellClass(),
                entity.getPrice(),
                entity.getCreatedAt()
        )
                : null;
    }
}
