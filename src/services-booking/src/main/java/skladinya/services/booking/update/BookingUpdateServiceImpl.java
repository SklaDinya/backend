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

@Service
public class BookingUpdateServiceImpl implements BookingUpdateService {

    private final BookingRepository bookingRepository;
    private final long paymentTimeoutMinutes;

    public BookingUpdateServiceImpl(BookingRepository bookingRepository,
                                    @Value("${skladinya.booking.payment-timeout-minutes}")
                                    long paymentTimeoutMinutes) {
        this.bookingRepository = bookingRepository;
        this.paymentTimeoutMinutes = paymentTimeoutMinutes;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateBookingsStatuses() {

        LocalDateTime now = LocalDateTime.now();
        int pageSize = 1000;
        int pageNumber = 0;

        while (true) {

            List<Booking> bookings = bookingRepository.getAllForOperator(
                    null,
                    new BookingSearchOptions(
                            null, null,
                            List.of(BookingStatus.Created, BookingStatus.Paid, BookingStatus.InProcess),
                            pageSize, pageNumber));

            if (bookings.isEmpty()) {
                break;
            }

            for (Booking booking : bookings) {

                switch (booking.status()) {

                    case Created -> {
                        if (booking.createdAt()
                                .plusMinutes(paymentTimeoutMinutes)
                                .isBefore(now)) {
                            bookingRepository.updateStatus(
                                    booking.bookingId(),
                                    BookingStatus.Cancelled
                            );
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
            pageNumber++;
        }
    }
}
