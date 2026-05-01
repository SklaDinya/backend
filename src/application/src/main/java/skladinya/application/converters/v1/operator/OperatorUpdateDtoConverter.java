package skladinya.application.converters.v1.operator;

import skladinya.application.dtos.v1.operator.OperatorUpdateDto;
import skladinya.domain.models.operator.OperatorUpdate;

public final class OperatorUpdateDtoConverter {

    private OperatorUpdateDtoConverter() {
    }

    public static OperatorUpdate toCoreEntity(OperatorUpdateDto dto) {
        return new OperatorUpdate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                OperatorRoleDtoConverter.toCoreEntity(dto.getRole()),
                dto.getBanned()
        );
    }
}
