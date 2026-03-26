package skladinya.domain.models.booking;

import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
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
        Date startTime,
        Duration bookingTime,
        Date endTime,
        Date createdAt,
        BookingStatus status,
        BigDecimal price,
        Payment payment
) {

    public Booking(
            UUID userId,
            UUID storageId,
            List<UUID> cellIds,
            Date startTime,
            Duration bookingTime,
            Date endTime,
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
                new Date(),
                BookingStatus.Created,
                price,
                null
        );
    }
}
