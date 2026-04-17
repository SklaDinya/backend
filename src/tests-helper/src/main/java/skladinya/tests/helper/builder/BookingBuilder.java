package skladinya.tests.helper.builder;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BookingBuilder {

    private UUID bookingId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();
    private User user = null;
    private UUID storageId = UUID.randomUUID();
    private Storage storage = null;
    private List<UUID> cellIds = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private LocalDateTime startTime = LocalDateTime.now();
    private Duration bookingTime = Duration.ZERO;
    private LocalDateTime endTime = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
    private BookingStatus status = BookingStatus.Created;
    private BigDecimal price = BigDecimal.ZERO;
    private Payment payment = null;

    private BookingBuilder() {
    }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public BookingBuilder bookingId(UUID bookingId) {
        this.bookingId = bookingId;
        return this;
    }

    public BookingBuilder user(User user) {
        this.user = user;
        this.userId = user.userId();
        return this;
    }

    public BookingBuilder storage(Storage storage) {
        this.storage = storage;
        this.storageId = storage.storageId();
        return this;
    }

    public BookingBuilder cells(List<Cell> cells) {
        this.cells = cells.stream().toList();
        this.cellIds = cells.stream().map(Cell::cellId).toList();
        return this;
    }

    public BookingBuilder startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public BookingBuilder bookingTime(Duration bookingTime) {
        this.bookingTime = bookingTime;
        return this;
    }

    public BookingBuilder endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public BookingBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public BookingBuilder status(BookingStatus status) {
        this.status = status;
        return this;
    }

    public BookingBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BookingBuilder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Booking build() {
        return new Booking(
                bookingId,
                userId,
                user,
                storageId,
                storage,
                cellIds,
                cells,
                startTime,
                bookingTime,
                endTime,
                createdAt,
                status,
                price,
                payment
        );
    }
}
