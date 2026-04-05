package skladinya.persistence.repositories;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingRepositoryPostgres implements BookingRepository {
    @Override
    public Booking create(Booking booking) {
        return null;
    }

    @Override
    public Optional<Booking> getByBookingId(UUID bookingId) {
        return Optional.empty();
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
    public Booking updateStatus(UUID bookingId, BookingStatus status) {
        return null;
    }

    @Override
    public Booking delete(UUID bookingId) {
        return null;
    }
}
