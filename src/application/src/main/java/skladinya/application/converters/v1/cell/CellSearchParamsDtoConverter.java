package skladinya.application.converters.v1.cell;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.cell.CellMySearchParamsDto;
import skladinya.application.dtos.v1.cell.CellSearchParamsDto;
import skladinya.domain.models.cell.CellSearchOptions;

import java.util.List;

public final class CellSearchParamsDtoConverter {

    private CellSearchParamsDtoConverter() {
    }

    public static CellSearchOptions toCoreEntity(CellSearchParamsDto dto) {
        return new CellSearchOptions(
                TimeConverter.toLocalDateTime(dto.getStartBooking()),
                dto.getTimeBooking(),
                dto.getCellClasses() == null ? List.of() : dto.getCellClasses(),
                dto.getPageSize() == null ? 50 : dto.getPageSize(),
                dto.getPageNumber() == null ? 0 : dto.getPageNumber()
        );
    }

    public static CellSearchOptions toCoreEntity(CellMySearchParamsDto dto) {
        return new CellSearchOptions(
                null,
                null,
                dto.getCellClasses() == null ? List.of() : dto.getCellClasses(),
                dto.getPageSize() == null ? 50 : dto.getPageSize(),
                dto.getPageNumber() == null ? 0 : dto.getPageNumber()
        );
    }
}
