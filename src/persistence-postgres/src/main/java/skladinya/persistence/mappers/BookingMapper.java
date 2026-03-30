package skladinya.persistence.mappers;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.payment.Payment;
import skladinya.persistence.entities.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingEntity toEntity(
            Booking booking,
            UserEntity user,
            StorageEntity storage,
            Set<CellEntity> cells,
            PaymentEntity payment
    ) {
        if (booking == null) {
            return null;
        }

        BookingEntity entity = new BookingEntity(
                booking.bookingId(),
                booking.startTime(),
                booking.bookingTime() != null ? booking.bookingTime().toHours() : null,
                booking.createdAt(),
                booking.status(),
                storage,
                cells,
                user
        );

        if (payment != null) {
            payment.setBooking(entity); // важно для bidirectional связи
            entity.setPayment(payment);
        }

        return entity;
    }

    public static Booking toDomain(BookingEntity entity) {
        if (entity == null) {
            return null;
        }

        UUID userId = entity.getUser() != null
                ? entity.getUser().getId()
                : null;

        UUID storageId = entity.getStorage() != null
                ? entity.getStorage().getId()
                : null;

        List<UUID> cellIds = entity.getCells() != null
                ? entity.getCells().stream()
                .map(CellEntity::getId)
                .collect(Collectors.toList())
                : null;

        List<Cell> cells = null;

        Payment payment = null;

        return new Booking(
                entity.getId(),
                userId,
                null,
                storageId,
                null,
                cellIds,
                cells,
                entity.getStartTime(),
                entity.getBookingTimeHour() != null
                        ? Duration.ofHours(entity.getBookingTimeHour())
                        : null,
                null, // endTime можно вычислять в domain
                entity.getCreatedAt(),
                entity.getStatus(),
                null, // price не хранится в entity
                payment
        );
    }
}
