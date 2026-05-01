package skladinya.application.converters.v1.operator;

import skladinya.application.dtos.v1.operator.OperatorRoleDto;
import skladinya.domain.models.operator.OperatorRole;

public final class OperatorRoleDtoConverter {

    private OperatorRoleDtoConverter() {
    }

    public static OperatorRoleDto toDto(OperatorRole role) {
        return switch (role) {
            case OperatorRole.MainOperator -> OperatorRoleDto.MainOperator;
            case OperatorRole.OrdinaryOperator -> OperatorRoleDto.OrdinaryOperator;
        };
    }

    public static OperatorRole toCoreEntity(OperatorRoleDto dto) {
        return switch (dto) {
            case OperatorRoleDto.MainOperator -> OperatorRole.MainOperator;
            case OperatorRoleDto.OrdinaryOperator -> OperatorRole.OrdinaryOperator;
        };
    }
}
