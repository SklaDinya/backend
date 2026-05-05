package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.storage.StorageCreateDtoConverter;
import skladinya.application.converters.v1.storage.StorageGetDtoConverter;
import skladinya.application.converters.v1.storage.StorageSearchParamsDtoConverter;
import skladinya.application.converters.v1.storage.StorageUpdateDtoConverter;
import skladinya.application.dtos.v1.storage.StorageCreateDto;
import skladinya.application.dtos.v1.storage.StorageGetDto;
import skladinya.application.dtos.v1.storage.StorageSearchParamsDto;
import skladinya.application.dtos.v1.storage.StorageUpdateDto;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.StorageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storages")
public class StorageController {
    
    private final StorageService storageService;
    
    private final RoleChecker roleChecker;

    public StorageController(StorageService storageService, RoleChecker roleChecker) {
        this.storageService = storageService;
        this.roleChecker = roleChecker;
    }

    @GetMapping
    public List<StorageGetDto> getAll(@Valid StorageSearchParamsDto dto) {
        var result = storageService.getAllBySearchOptions(
                StorageSearchParamsDtoConverter.toStoragesCoreEntity(dto));
        return result.stream().map(StorageGetDtoConverter::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody StorageCreateDto dto) {
        storageService.create(StorageCreateDtoConverter.toCoreEntity(dto));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requests")
    public List<StorageGetDto> getRequests(@RequestHeader String authorization, @Valid StorageSearchParamsDto dto) {
        roleChecker.requireAdmin(authorization);
        var result = storageService.getAllBySearchOptions(
                StorageSearchParamsDtoConverter.toRequestsCoreEntity(dto));
        return result.stream().map(StorageGetDtoConverter::toDto).toList();
    }

    @GetMapping("/{storageId}")
    public StorageGetDto get(@PathVariable UUID storageId) {
        var result = storageService.getByStorageId(storageId);
        return StorageGetDtoConverter.toDto(result);
    }

    @PatchMapping("/{storageId}")
    public StorageGetDto update(
            @RequestHeader String authorization,
            @PathVariable UUID storageId,
            @Valid @RequestBody StorageUpdateDto dto
    ) {
        roleChecker.requireAdmin(authorization);
        var result = storageService.update(storageId, StorageUpdateDtoConverter.toCoreEntity(dto));
        return StorageGetDtoConverter.toDto(result);
    }

    @PatchMapping("/{storageId}/approve")
    public StorageGetDto approve(@RequestHeader String authorization, @PathVariable UUID storageId) {
        roleChecker.requireAdmin(authorization);
        var result = storageService.approve(storageId);
        return StorageGetDtoConverter.toDto(result);
    }

    @DeleteMapping("/{storageId}/reject")
    public ResponseEntity<Void> reject(@RequestHeader String authorization, @PathVariable UUID storageId) {
        roleChecker.requireAdmin(authorization);
        storageService.reject(storageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public StorageGetDto getMy(@RequestHeader String authorization) {
        var storageId = roleChecker.requireStorageOperator(authorization).storageId();
        var result = storageService.getByStorageId(storageId);
        return StorageGetDtoConverter.toDto(result);
    }

    @PatchMapping("/my")
    public StorageGetDto updateMy(
            @RequestHeader String authorization,
            @Valid @RequestBody StorageUpdateDto dto
    ) {
        var storageId = roleChecker.requireStorageOperator(authorization).storageId();
        var result = storageService.update(storageId, StorageUpdateDtoConverter.toCoreEntity(dto));
        return StorageGetDtoConverter.toDto(result);
    }
}
