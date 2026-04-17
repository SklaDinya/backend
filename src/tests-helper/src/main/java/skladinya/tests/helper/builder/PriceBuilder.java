package skladinya.tests.helper.builder;

import skladinya.domain.models.price.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class PriceBuilder {

    private UUID priceId = UUID.randomUUID();
    private UUID storageId = UUID.randomUUID();
    private String cellClass = "Млекопитающие";
    private BigDecimal price = BigDecimal.valueOf(67);
    private LocalDateTime createdAt = LocalDateTime.now();

    private PriceBuilder() {
    }

    public static PriceBuilder builder() {
        return new PriceBuilder();
    }

    public PriceBuilder priceId(UUID priceId) {
        this.priceId = priceId;
        return this;
    }

    public PriceBuilder storageId(UUID storageId) {
        this.storageId = storageId;
        return this;
    }

    public PriceBuilder cellClass(String cellClass) {
        this.cellClass = cellClass;
        return this;
    }

    public PriceBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public PriceBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Price build() {
        return new Price(priceId, storageId, cellClass, price, createdAt);
    }
}
