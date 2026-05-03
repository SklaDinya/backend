package skladinya.application.converters.v1.price;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.price.PriceGetDto;
import skladinya.domain.models.price.Price;

public final class PriceGetDtoConverter {

    private PriceGetDtoConverter() {
    }

    public static PriceGetDto toDto(Price price) {
        return PriceGetDto.builder()
                .priceId(price.priceId())
                .storageId(price.storageId())
                .cellClass(price.cellClass())
                .price(price.price())
                .createdAt(TimeConverter.toOffsetDateTime(price.createdAt()))
                .build();
    }
}
