package skladinya.application.converters.v1.operator;

import skladinya.application.dtos.v1.operator.OperatorCreateDto;
import skladinya.domain.models.operator.OperatorCreate;

public final class OperatorCreateDtoConverter {

    private OperatorCreateDtoConverter() {
    }

    public static OperatorCreate toCoreEntity(OperatorCreateDto dto) {
        return new OperatorCreate(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getUsername(),
                OperatorRoleDtoConverter.toCoreEntity(dto.getRole())
        );
    }
}
