package skladinya.domain.models.price;

import java.math.BigDecimal;

public record PriceCreate(
        String cellClass,
        BigDecimal price
) {
}
