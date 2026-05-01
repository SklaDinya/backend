package skladinya.application.converters.v1.cell;

import skladinya.application.dtos.v1.cell.CellCreateDto;
import skladinya.domain.models.cell.CellCreate;

public final class CellCreateDtoConverter {

    private CellCreateDtoConverter() {
    }

    public static CellCreate toCoreEntity(CellCreateDto dto) {
        return new CellCreate(dto.getName(), dto.getCellClass());
    }
}
