package skladinya.domain.models.price;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record Price(
        UUID priceId,
        UUID storageId,
        String cellClass,
        BigDecimal price,
        Date createdAt
) {

    public Price(UUID storageId, String cellClass, BigDecimal price) {
        this(
                UUID.randomUUID(),
                storageId,
                cellClass,
                price,
                new Date()
        );
    }
}
