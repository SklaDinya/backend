package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.repositories.BookingRepository;
import skladinya.persistence.entities.*;
import skladinya.persistence.mappers.BookingMapper;
import skladinya.persistence.mappers.PaymentMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookingRepositoryPostgres implements BookingRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Booking create(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        UserEntity user = em.find(UserEntity.class, booking.userId());
        StorageEntity storage = em.find(StorageEntity.class, booking.storageId());

        Set<CellEntity> cells = booking.cellIds() != null
                ? booking.cellIds().stream()
                .map(id -> em.find(CellEntity.class, id))
                .collect(Collectors.toSet())
                : Set.of();

        PaymentEntity payment = null;
        if (booking.payment() != null) {
            payment = PaymentMapper.toEntity(booking.payment(), null);
        }

        BookingEntity entity = BookingMapper.toEntity(booking, user, storage, cells, payment);

        em.persist(entity);
        em.flush();

        return BookingMapper.toDomain(entity);
    }

    @Override
    public Optional<Booking> getByBookingId(UUID bookingId) {
        if (bookingId == null) return Optional.empty();

        var query = em.createQuery(
                "SELECT b FROM BookingEntity b WHERE b.id = :id",
                BookingEntity.class
        );
        query.setParameter("id", bookingId);

        return query.getResultStream()
                .findFirst()
                .map(BookingMapper::toDomain);
    }

    @Override
    public List<Booking> getAllForUser(UUID userId, int pageSize, int pageNumber) {
        if (userId == null) return List.of();

        var query = em.createQuery(
                "SELECT b FROM BookingEntity b WHERE b.user.id = :userId ORDER BY b.createdAt DESC",
                BookingEntity.class
        );
        query.setParameter("userId", userId);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultStream()
                .map(BookingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getAllForOperator(UUID storageId, BookingSearchOptions options) {
        if (storageId == null) return List.of();

        StringBuilder sb = new StringBuilder("SELECT b FROM BookingEntity b WHERE b.storage.id = :storageId");

        if (options.startBooking() != null) sb.append(" AND b.startTime >= :startBooking");
        if (options.endBooking() != null) sb.append(" AND b.startTime <= :endBooking");
        if (options.statuses() != null && !options.statuses().isEmpty()) sb.append(" AND b.status IN :statuses");

        sb.append(" ORDER BY b.createdAt DESC");

        var query = em.createQuery(sb.toString(), BookingEntity.class);
        query.setParameter("storageId", storageId);

        if (options.startBooking() != null) query.setParameter("startBooking", options.startBooking());
        if (options.endBooking() != null) query.setParameter("endBooking", options.endBooking());
        if (options.statuses() != null && !options.statuses().isEmpty())
            query.setParameter("statuses", options.statuses());

        query.setFirstResult(options.pageNumber() * options.pageSize());
        query.setMaxResults(options.pageSize());

        return query.getResultStream()
                .map(BookingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Booking updateStatus(UUID bookingId, BookingStatus status) {
        if (bookingId == null || status == null) return null;

        BookingEntity entity = em.find(BookingEntity.class, bookingId);
        if (entity == null) return null;

        entity.setStatus(status);
        em.merge(entity);
        em.flush();

        return BookingMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Booking delete(UUID bookingId) {
        if (bookingId == null) return null;

        BookingEntity entity = em.find(BookingEntity.class, bookingId);
        if (entity == null) return null;

        em.remove(entity);
        em.flush();

        return BookingMapper.toDomain(entity);
    }
}
