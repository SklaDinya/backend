package skladinya.application.converters.v1.price;

import skladinya.application.dtos.v1.price.PriceCreateDto;
import skladinya.domain.models.price.PriceCreate;

public class PriceCreateDtoConverter {

    private PriceCreateDtoConverter() {
    }

    public static PriceCreate toCoreEntity(PriceCreateDto dto) {
        return new PriceCreate(
                dto.getCellClass(),
                dto.getPrice()
        );
    }
}
