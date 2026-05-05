package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.cell.CellCreateDtoConverter;
import skladinya.application.converters.v1.cell.CellGetDtoConverter;
import skladinya.application.converters.v1.cell.CellSearchParamsDtoConverter;
import skladinya.application.dtos.v1.cell.CellCreateDto;
import skladinya.application.dtos.v1.cell.CellGetDto;
import skladinya.application.dtos.v1.cell.CellMySearchParamsDto;
import skladinya.application.dtos.v1.cell.CellSearchParamsDto;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.CellService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storages")
public class CellController {

    private final CellService cellService;

    private final RoleChecker roleChecker;

    public CellController(CellService cellService, RoleChecker roleChecker) {
        this.cellService = cellService;
        this.roleChecker = roleChecker;
    }

    @GetMapping("/{storageId}/cells")
    public List<CellGetDto> getAll(@PathVariable UUID storageId, @Valid CellSearchParamsDto dto) {
        var result = cellService.getAllBySearchOptions(storageId,
                CellSearchParamsDtoConverter.toCoreEntity(dto));
        return result.stream().map(CellGetDtoConverter::toDto).toList();
    }

    @GetMapping("/{storageId}/cells/classes")
    public List<String> getAllClasses(@PathVariable UUID storageId) {
        return cellService.getAllCellClasses(storageId);
    }

    @GetMapping("/my/cells")
    public List<CellGetDto> getAllMy(@RequestHeader String authorization, @Valid CellMySearchParamsDto dto) {
        var storageId = roleChecker.requireStorageOperator(authorization).storageId();
        var result = cellService.getAllBySearchOptions(storageId,
                CellSearchParamsDtoConverter.toCoreEntity(dto));
        return result.stream().map(CellGetDtoConverter::toDto).toList();
    }

    @PostMapping("/my/cells")
    public ResponseEntity<CellGetDto> createMy(
            @RequestHeader String authorization,
            @Valid @RequestBody CellCreateDto dto
    ) {
        var storageId = roleChecker.requireStorageOperator(authorization).storageId();
        var result = cellService.create(storageId, CellCreateDtoConverter.toCoreEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CellGetDtoConverter.toDto(result));
    }

    @GetMapping("/my/cells/classes")
    public List<String> getAllMyClasses(@RequestHeader String authorization) {
        var storageId = roleChecker.requireStorageOperator(authorization).storageId();
        return cellService.getAllCellClasses(storageId);
    }
}
