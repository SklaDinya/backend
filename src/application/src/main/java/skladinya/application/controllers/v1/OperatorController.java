package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.operator.OperatorCreateDtoConverter;
import skladinya.application.converters.v1.operator.OperatorGetDtoConverter;
import skladinya.application.converters.v1.operator.OperatorSearchParamsDtoConverter;
import skladinya.application.converters.v1.operator.OperatorUpdateDtoConverter;
import skladinya.application.dtos.v1.operator.OperatorCreateDto;
import skladinya.application.dtos.v1.operator.OperatorGetDto;
import skladinya.application.dtos.v1.operator.OperatorSearchParamsDto;
import skladinya.application.dtos.v1.operator.OperatorUpdateDto;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.OperatorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storages/my/operators")
public class OperatorController {

    private final OperatorService operatorService;

    private final RoleChecker roleChecker;

    public OperatorController(OperatorService operatorService, RoleChecker roleChecker) {
        this.operatorService = operatorService;
        this.roleChecker = roleChecker;
    }

    @GetMapping
    public List<OperatorGetDto> getAll(@RequestHeader String authorization, @Valid OperatorSearchParamsDto dto) {
        var storageId = roleChecker.requireMainStorageOperator(authorization).storageId();
        var result = operatorService.getAllBySearchOptions(storageId,
                OperatorSearchParamsDtoConverter.toCoreEntity(dto));
        return result.stream().map(OperatorGetDtoConverter::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<OperatorGetDto> create(
            @RequestHeader String authorization,
            @Valid @RequestBody OperatorCreateDto dto
    ) {
        var storageId = roleChecker.requireMainStorageOperator(authorization).storageId();
        var result = operatorService.create(storageId, OperatorCreateDtoConverter.toCoreEntity(dto), false);
        return ResponseEntity.status(HttpStatus.CREATED).body(OperatorGetDtoConverter.toDto(result));
    }

    @GetMapping("/{operatorId}")
    public OperatorGetDto get(@RequestHeader String authorization, @PathVariable UUID operatorId) {
        var storageId = roleChecker.requireMainStorageOperator(authorization).storageId();
        var result = operatorService.getByOperatorId(storageId, operatorId);
        return OperatorGetDtoConverter.toDto(result);
    }

    @PatchMapping("/{operatorId}")
    public OperatorGetDto update(
            @RequestHeader String authorization,
            @PathVariable UUID operatorId,
            @Valid @RequestBody OperatorUpdateDto dto
    ) {
        var storageId = roleChecker.requireMainStorageOperator(authorization).storageId();
        var result = operatorService.update(storageId, operatorId,
                OperatorUpdateDtoConverter.toCoreEntity(dto));
        return OperatorGetDtoConverter.toDto(result);
    }
}
