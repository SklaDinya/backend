package skladinya.application.converters.v1.operator;

import skladinya.application.dtos.v1.operator.OperatorSearchParamsDto;
import skladinya.domain.models.operator.OperatorSearchOptions;

public final class OperatorSearchParamsDtoConverter {

    private  OperatorSearchParamsDtoConverter() {
    }

    public static OperatorSearchOptions toCoreEntity(OperatorSearchParamsDto dto) {
        return new OperatorSearchOptions(
                dto.getUsername(),
                dto.getName(),
                dto.getEmail(),
                dto.getRole() == null ? null : OperatorRoleDtoConverter.toCoreEntity(dto.getRole()),
                dto.getPageSize() == null ? 50 : dto.getPageSize(),
                dto.getPageNumber() == null ? 0 : dto.getPageNumber()
        );
    }
}
