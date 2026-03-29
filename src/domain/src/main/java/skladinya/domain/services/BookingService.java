package skladinya.domain.services;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingCreate;
import skladinya.domain.models.booking.BookingReceipt;
import skladinya.domain.models.booking.BookingSearchOptions;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingReceipt create(UUID userId, BookingCreate createForm);

    Booking getByBookingId(UUID userId, UUID bookingId);

    List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber);

    List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options);

    Booking payNoOp(UUID userId, String receipt);

    Booking payRandom(UUID userId, String receipt);

    Booking cancel(UUID userId, UUID bookingId);
}
