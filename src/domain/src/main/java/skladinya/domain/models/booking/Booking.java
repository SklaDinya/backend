package skladinya.domain.models.booking;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Booking(
        UUID bookingId,
        UUID userId,
        User user,
        UUID storageId,
        Storage storage,
        List<UUID> cellIds,
        List<Cell> cells,
        LocalDateTime startTime,
        Duration bookingTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        BookingStatus status,
        BigDecimal price,
        Payment payment
) {

    public Booking(
            UUID userId,
            UUID storageId,
            List<UUID> cellIds,
            LocalDateTime startTime,
            Duration bookingTime,
            LocalDateTime endTime,
            BigDecimal price
    ) {
        this(
                UUID.randomUUID(),
                userId,
                null,
                storageId,
                null,
                cellIds,
                List.of(),
                startTime,
                bookingTime,
                endTime,
                LocalDateTime.now(),
                BookingStatus.Created,
                price,
                null
        );
    }
}
