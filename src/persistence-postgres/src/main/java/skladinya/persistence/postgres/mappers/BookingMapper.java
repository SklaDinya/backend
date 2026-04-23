package skladinya.persistence.postgres.mappers;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.cell.Cell;
import skladinya.persistence.postgres.entities.BookingEntity;
import skladinya.persistence.postgres.mappers.enums.BookingStatusMapper;
import skladinya.persistence.postgres.mappers.enums.PaymentTypeMapper;
import skladinya.persistence.postgres.mappers.helpers.PaymentPayloadResolver;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingEntity toEntity(Booking booking) {
        BookingEntity entity = booking != null ? new BookingEntity() : null;

        if (entity != null) {
            entity.setId(booking.bookingId());
            entity.setStartTime(booking.startTime());
            entity.setBookingTimeHour(booking.bookingTime() != null ? booking.bookingTime().toHours() : 0);
            entity.setCreatedAt(booking.createdAt());
            entity.setEndTime(booking.endTime());
            entity.setPrice(booking.price());
            entity.setStatus(BookingStatusMapper.toEntity(booking.status()));
            entity.setUserId(booking.userId());
            entity.setStorageId(booking.storageId());

            if (booking.cells() != null) {
                entity.setCells(booking.cells().stream()
                        .map(CellMapper::toEntity)
                        .collect(Collectors.toSet()));
            }

            if (booking.payment() != null) {
                entity.setPayment(PaymentMapper.toEntity(booking.payment()));
            }
        }

        return entity;
    }

    public static Booking toDomain(BookingEntity entity) {
        List<Cell> cells = entity.getCells() != null
                ? entity.getCells().stream()
                .map(CellMapper::toDomain)
                .toList()
                : List.of();

        return new Booking(
        entity.getId(),
        entity.getUserId(),
        UserMapper.toDomain(entity.getUser()),
        entity.getStorageId(),
        StorageMapper.toDomain(entity.getStorage()),
        cells.stream().map(Cell::cellId).toList(),
        cells,
        entity.getStartTime(),
        entity.getBookingTimeHour() != null
                ? Duration.ofHours(entity.getBookingTimeHour())
                : null,
        entity.getEndTime(),
        entity.getCreatedAt(),
        BookingStatusMapper.toDomain(entity.getStatus()),
        entity.getPrice(),
        entity.getPayment() != null
                ? PaymentMapper.toDomain(
                        entity.getPayment(),
                        PaymentPayloadResolver.resolve(PaymentTypeMapper.toDomain(entity.getPayment().getPaymentType()))
                )
                : null
        );
    }
}
