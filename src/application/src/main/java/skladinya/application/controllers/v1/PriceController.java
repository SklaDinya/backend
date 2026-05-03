package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.price.PriceCreateDtoConverter;
import skladinya.application.converters.v1.price.PriceGetDtoConverter;
import skladinya.application.dtos.v1.price.PriceCreateDto;
import skladinya.application.dtos.v1.price.PriceGetDto;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.PriceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storages")
public class PriceController {
    private final PriceService priceService;
    private final RoleChecker roleChecker;

    public PriceController(PriceService priceService, RoleChecker roleChecker) {
        this.priceService = priceService;
        this.roleChecker = roleChecker;
    }

    @GetMapping("/{storageId}/prices")
    public List<PriceGetDto> getPricesByStorageId(@PathVariable UUID storageId) {
        var result = priceService.getAllByStorageId(storageId);
        return result.stream().map(PriceGetDtoConverter::toDto).toList();
    }

    @GetMapping("/my/prices")
    public List<PriceGetDto> getMyPrices(@RequestHeader String authorization) {
        var data = roleChecker.requireStorageOperator(authorization);
        var result = priceService.getAllByStorageId(data.storageId());
        return result.stream().map(PriceGetDtoConverter::toDto).toList();
    }

    @PostMapping("/my/prices")
    public ResponseEntity<Void> createPrice(
            @RequestHeader String authorization,
            @Valid @RequestBody PriceCreateDto dto) {
        var data = roleChecker.requireStorageOperator(authorization);
        priceService.create(data.storageId(), PriceCreateDtoConverter.toCoreEntity(dto));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
