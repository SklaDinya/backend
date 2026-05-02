package skladinya.application.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skladinya.application.converters.v1.booking.*;
import skladinya.application.dtos.v1.booking.*;
import skladinya.application.security.RoleChecker;
import skladinya.domain.services.BookingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;
    private final RoleChecker roleChecker;

    public BookingController(BookingService bookingService, RoleChecker roleChecker) {
        this.bookingService = bookingService;
        this.roleChecker = roleChecker;
    }

    @GetMapping("/users/me/bookings")
    public List<BookingGetUserDto> getUserBookings(
            @RequestHeader String authorization,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.getAllForUser(data.userId(), pageSize, pageNumber);
        return result.stream().map(BookingGetUserDtoConverter::toDto).toList();
    }

    @PostMapping("/users/me/bookings")
    public ResponseEntity<BookingReceiptDto> createBooking(
            @RequestHeader String authorization,
            @Valid @RequestBody BookingCreateDto dto) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.create(data.userId(), BookingCreateDtoConverter.toCoreEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingReceiptDtoConverter.toDto(result));
    }

    @GetMapping("/users/me/bookings/{bookingId}")
    public BookingGetUserDto getBookingById(
            @RequestHeader String authorization,
            @PathVariable UUID bookingId) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.getByBookingId(data.userId(), bookingId);
        return BookingGetUserDtoConverter.toDto(result);
    }

    @DeleteMapping("/users/me/bookings/{bookingId}")
    public BookingGetUserDto cancelBooking(
            @RequestHeader String authorization,
            @PathVariable UUID bookingId) {
        var data = roleChecker.requireClient(authorization);
        var result = bookingService.cancel(data.userId(), bookingId);
        return BookingGetUserDtoConverter.toDto(result);
    }

    @GetMapping("/storages/my/bookings")
    public List<BookingGetOperatorDto> getOperatorBookings(
            @RequestHeader String authorization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startBooking,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endBooking,
            @RequestParam(required = false) List<BookingStatusDto> statuses,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize) {
        var data = roleChecker.requireStorageOperator(authorization);
        var options = new BookingSearchParametersDto();
        options.setStartBooking(startBooking);
        options.setEndBooking(endBooking);
        options.setStatuses(statuses);
        options.setPageNumber(pageNumber);
        options.setPageSize(pageSize);
        var result = bookingService.getAllForOperator(data.storageId(), BookingSearchParametersDtoConverter.toCoreEntity(options));
        return result.stream().map(BookingGetOperatorDtoConverter::toDto).toList();
    }
}
