package skladinya.application.converters.v1.operator;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.operator.OperatorGetDto;
import skladinya.domain.models.operator.Operator;

public final class OperatorGetDtoConverter {

    private OperatorGetDtoConverter() {
    }

    public static OperatorGetDto toDto(Operator operator) {
        return new OperatorGetDto(
                operator.operatorId(),
                operator.user().username(),
                operator.user().name(),
                operator.user().email(),
                OperatorRoleDtoConverter.toDto(operator.role()),
                operator.user().banned(),
                TimeConverter.toOffsetDateTime(operator.user().createdAt()),
                TimeConverter.toOffsetDateTime(operator.user().updatedAt())
        );
    }
}
