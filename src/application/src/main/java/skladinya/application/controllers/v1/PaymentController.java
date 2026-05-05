package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.booking.BookingGetUserDtoConverter;
import skladinya.application.dtos.v1.booking.BookingGetUserDto;
import skladinya.application.dtos.v1.payment.PaymentNoOpDto;
import skladinya.application.dtos.v1.payment.PaymentRandomDto;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.BookingService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final BookingService bookingService;
    private final RoleChecker roleChecker;

    public PaymentController(BookingService bookingService, RoleChecker roleChecker) {
        this.bookingService = bookingService;
        this.roleChecker = roleChecker;
    }

    @PostMapping("/noop")
    public BookingGetUserDto payNoOp(
            @RequestHeader String authorization,
            @Valid @RequestBody PaymentNoOpDto dto) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.payNoOp(data.userId(), dto.getReceipt());
        return BookingGetUserDtoConverter.toDto(result);
    }

    @PostMapping("/random")
    public BookingGetUserDto payRandom(
            @RequestHeader String authorization,
            @Valid @RequestBody PaymentRandomDto dto) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.payRandom(data.userId(), dto.getReceipt());
        return BookingGetUserDtoConverter.toDto(result);
    }
}
