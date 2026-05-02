package skladinya.services.booking.update;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.services.BookingUpdateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingUpdateServiceImpl implements BookingUpdateService {

    private final BookingRepository bookingRepository;
    private final long paymentTimeoutMinutes;

    public BookingUpdateServiceImpl(BookingRepository bookingRepository,
                                    @Value("${skladinya.booking.payment-timeout-minutes}") long paymentTimeoutMinutes) {
        this.bookingRepository = bookingRepository;
        this.paymentTimeoutMinutes = paymentTimeoutMinutes;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void setDelayedCancel(UUID bookingId) { // TODO мб сделать private
        Booking booking = bookingRepository.getByBookingId(bookingId)
                .orElseThrow();

        if (booking.status() == BookingStatus.Created) {
            bookingRepository.updateStatus(
                    bookingId,
                    BookingStatus.Cancelled
            );
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateBookingsStatuses() {

        LocalDateTime now = LocalDateTime.now();
        int pageSize = 1000;

        while (true) {

            List<Booking> bookings = bookingRepository.getAllForOperator(
                    null,
                    new BookingSearchOptions(
                            null, null,
                            List.of(BookingStatus.Created, BookingStatus.Paid, BookingStatus.InProcess),
                            pageSize, 0));

            if (bookings.isEmpty()) {
                break;
            }

            for (Booking booking : bookings) {

                switch (booking.status()) {

                    case Created -> {
                        if (booking.createdAt()
                                .plusMinutes(paymentTimeoutMinutes)
                                .isBefore(now)) {

                            setDelayedCancel(booking.bookingId());
                        }
                    }
                    case Paid -> {
                        if (!booking.startTime().isAfter(now)) {
                            bookingRepository.updateStatus(
                                    booking.bookingId(),
                                    BookingStatus.InProcess
                            );
                        }
                    }
                    case InProcess -> {
                        if (booking.endTime().isBefore(now)) {
                            bookingRepository.updateStatus(booking.bookingId(), BookingStatus.Finished);
                        }
                    }
                }
            }
        }
    }
}
