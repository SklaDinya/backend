package skladinya.domain.services;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingCreate;
import skladinya.domain.models.booking.BookingSearchOptions;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    String create(BookingCreate createForm);

    Booking getByBookingId(UUID bookingId);

    List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber);

    List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options);

    Booking payNoOp(String receipt);

    Booking payRandom(String receipt);

    Booking cancel(UUID userId, UUID bookingId);
}
