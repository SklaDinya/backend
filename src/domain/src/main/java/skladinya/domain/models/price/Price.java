package skladinya.domain.models.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Price(
        UUID priceId,
        UUID storageId,
        String cellClass,
        BigDecimal price,
        LocalDateTime createdAt
) {

    public Price(UUID storageId, String cellClass, BigDecimal price) {
        this(
                UUID.randomUUID(),
                storageId,
                cellClass,
                price,
                LocalDateTime.now()
        );
    }
}
