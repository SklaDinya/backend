package skladinya.domain.repositories;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {

    Booking create(Booking booking);

    Optional<Booking> getByBookingId(UUID bookingId);

    List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber);

    List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options);

    Booking updateStatus(UUID bookingId, BookingStatus status);

    Booking delete(UUID bookingId);
}
