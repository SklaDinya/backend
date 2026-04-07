package skladinya.persistence.repositories;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.repositories.BookingRepository;
import skladinya.persistence.entities.BookingEntity;
import skladinya.persistence.entities.enums.BookingStatusEntity;
import skladinya.persistence.mappers.BookingMapper;
import skladinya.persistence.mappers.enums.BookingStatusMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringBookingRepository extends
        JpaRepository<BookingEntity, UUID>,
        JpaSpecificationExecutor<BookingEntity> {

    List<BookingEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}

class BookingSpecification {

    public static Specification<BookingEntity> byOptions(UUID storageId, BookingSearchOptions options) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (storageId != null) {
                predicates.add(cb.equal(root.get("storageId"), storageId));
            }

            if (options.statuses() != null && !options.statuses().isEmpty()) {
                List<BookingStatusEntity> entityStatuses = options.statuses()
                        .stream()
                        .map(BookingStatusMapper::toEntity)
                        .toList();
                predicates.add(root.get("status").in(entityStatuses));
            }

            if (options.startBooking() != null && options.endBooking() != null) {
                predicates.add(cb.or(
                        cb.lessThanOrEqualTo(root.get("endTime"), options.startBooking()),
                        cb.greaterThanOrEqualTo(root.get("startTime"), options.endBooking())
                ));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}

@Repository
@RequiredArgsConstructor
public class PostgresBookingRepository implements BookingRepository {

    private final SpringBookingRepository repo;

    @Override
    public Booking create(Booking booking) {
        BookingEntity entity = BookingMapper.toEntity(booking);
        BookingEntity saved = repo.save(entity);
        return BookingMapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> getByBookingId(UUID bookingId) {
        return repo.findById(bookingId)
                .map(BookingMapper::toDomain);
    }

    @Override
    public List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options) {
        Pageable pageable = PageRequest.of(options.pageNumber(), options.pageSize());

        return repo.findAll(BookingSpecification.byOptions(storageId, options), pageable)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public Booking updateStatus(UUID bookingId, BookingStatus status) {
        return repo.findById(bookingId)
                .map(existing -> {
                    existing.setStatus(BookingStatusMapper.toEntity(status));
                    return BookingMapper.toDomain(repo.save(existing));
                })
                .orElse(null);
    }

    @Override
    public Booking delete(UUID bookingId) {
        return repo.findById(bookingId)
                .map(existing -> {
                    repo.delete(existing);
                    return BookingMapper.toDomain(existing);
                })
                .orElse(null);
    }
}
