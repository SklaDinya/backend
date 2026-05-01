package skladinya.application.converters.v1.cell;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.cell.CellGetDto;
import skladinya.domain.models.cell.Cell;

public final class CellGetDtoConverter {

    private CellGetDtoConverter() {
    }

    public static CellGetDto toDto(Cell cell) {
        return new CellGetDto(
                cell.cellId(),
                cell.storageId(),
                cell.name(),
                cell.cellClass(),
                TimeConverter.toOffsetDateTime(cell.createdAt())
        );
    }
}
