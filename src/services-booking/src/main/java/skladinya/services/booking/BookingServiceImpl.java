package skladinya.services.booking;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.booking.*;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;
import skladinya.domain.models.price.Price;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.domain.services.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequestScope
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final StorageService storageService;
    private final CellService cellService;
    private final PriceService priceService;
    private final PaymentService paymentService;
    private final Synchronizer synchronizer;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserService userService,
            StorageService storageService,
            CellService cellService, PriceService priceService,
            PaymentService paymentService,
            Synchronizer synchronizer
    ) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.storageService = storageService;
        this.cellService = cellService;
        this.priceService = priceService;
        this.paymentService = paymentService;
        this.synchronizer = synchronizer;
    }

    @Override
    public BookingReceipt create(UUID userId, BookingCreate createForm) {
        return synchronizer.executeTransactionFunction(() -> {

            var user = userService.getByUserId(userId);
            if (user.banned()) {
                throw SklaDinyaException.invalidAccess("User banned");
            }

            var storage = storageService.getByStorageId(createForm.storageId());

            var cells = cellService.getAllByCellIds(createForm.storageId(), createForm.cellIds());
            if (cells.size() != createForm.cellIds().size()) {
                throw SklaDinyaException.notFound("Some cells not found");
            }

            LocalDateTime start = createForm.startTime();
            Duration duration = createForm.bookingTime();
            LocalDateTime end = start.plus(duration);

            BigDecimal price = calculatePrice(storage.storageId(), cells, duration);

            var booking = new Booking(
                    UUID.randomUUID(),
                    userId,
                    user,
                    storage.storageId(),
                    storage,
                    createForm.cellIds(),
                    cells,
                    start,
                    duration,
                    end,
                    LocalDateTime.now(),
                    BookingStatus.Created,
                    price,
                    null
            );

            var saved = bookingRepository.create(booking);

            return new BookingReceipt(saved, paymentService.createReceipt(booking));
        });
    }

    @Override
    public Booking getByBookingId(UUID userId, UUID bookingId) {
        return synchronizer.executeSingleFunction(() -> {
            var booking = bookingRepository.getByBookingId(bookingId)
                    .orElseThrow(() -> SklaDinyaException.notFound("Booking not found"));
            checkBookingOwner(userId, booking);

            return booking;
        });
    }

    @Override
    public List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber) {
        return synchronizer.executeSingleFunction(() -> {
            var bookings = bookingRepository.getAllForUser(userId, pageSize, pageNumber);
            bookings.forEach(booking -> checkBookingOwner(userId, booking));
            return bookings;
        });
    }

    @Override
    public List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options) {
        return synchronizer.executeSingleFunction(() ->
                bookingRepository.getAllForOperator(storageId, options));
    }

    @Override
    public Booking payNoOp(UUID userId, String receipt) {
        return synchronizer.executeTransactionFunction(() -> {
            var booking = paymentService.validateReceipt(receipt);
            checkBookingOwner(userId, booking);
            UUID bookingId = booking.bookingId();

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            paymentService.payNoOp(booking);

            return bookingRepository.updateStatus(bookingId, BookingStatus.Paid);
        });
    }

    @Override
    public Booking payRandom(UUID userId, String receipt) {
        return synchronizer.executeTransactionFunction(() -> {
            var booking = paymentService.validateReceipt(receipt);
            checkBookingOwner(userId, booking);
            UUID bookingId = booking.bookingId();

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            paymentService.payRandom(booking);

            return bookingRepository.updateStatus(bookingId, BookingStatus.Paid);
        });
    }

    @Override
    public Booking cancel(UUID userId, UUID bookingId) {
        return synchronizer.executeTransactionFunction(() -> {

            var booking = bookingRepository.getByBookingId(bookingId)
                    .orElseThrow(() -> SklaDinyaException.notFound("Booking not found"));
            checkBookingOwner(userId, booking);

            if (booking.status() == BookingStatus.Finished) {
                throw SklaDinyaException.conflict("Cannot cancel finished booking");
            }

            return bookingRepository.updateStatus(bookingId, BookingStatus.Cancelled);
        });
    }

    private void checkBookingOwner(UUID userId, Booking booking) {

        if (!booking.userId().equals(userId)) {
            throw SklaDinyaException.invalidAccess("Access denied");
        }
    }

    private BigDecimal calculatePrice(UUID storageId, List<Cell> cells, Duration duration) {
        BigDecimal hours = BigDecimal.valueOf(duration.toHours());

        Map<String, BigDecimal> priceMap = priceService.getAllByStorageId(storageId)
                .stream()
                .collect(Collectors.toMap(Price::cellClass, Price::price));

        return cells.stream()
                .map(cell -> Optional.ofNullable(priceMap.get(cell.cellClass()))
                        .orElseThrow(() -> SklaDinyaException.notFound("Price for cell not found")))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(hours);
    }
}
