package skladinya.services.booking;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingCreate;
import skladinya.domain.models.booking.BookingReceipt;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.services.BookingService;

import java.util.List;
import java.util.UUID;

@Service
@RequestScope
public class BookingServiceImpl implements BookingService {
    @Override
    public BookingReceipt create(UUID userId, BookingCreate createForm) {
        return null;
    }

    @Override
    public Booking getByBookingId(UUID userId, UUID bookingId) {
        return null;
    }

    @Override
    public List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber) {
        return List.of();
    }

    @Override
    public List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options) {
        return List.of();
    }

    @Override
    public Booking payNoOp(UUID userId, String receipt) {
        return null;
    }

    @Override
    public Booking payRandom(UUID userId, String receipt) {
        return null;
    }

    @Override
    public Booking cancel(UUID userId, UUID bookingId) {
        return null;
    }
}
