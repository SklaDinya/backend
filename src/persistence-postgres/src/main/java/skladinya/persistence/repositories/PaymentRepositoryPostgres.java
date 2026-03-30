package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.persistence.entities.BookingEntity;
import skladinya.persistence.entities.PaymentEntity;
import skladinya.persistence.mappers.PaymentMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepositoryPostgres implements PaymentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Payment create(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        PaymentEntity entity = PaymentMapper.toEntity(payment, null);

        if (payment.bookingId() != null) {
            BookingEntity bookingEntity = em.find(BookingEntity.class, payment.bookingId());
            if (bookingEntity != null) {
                entity.setBooking(bookingEntity);
            }
        }

        em.persist(entity);
        em.flush();

        return PaymentMapper.toDomain(entity);
    }

    @Override
    public Optional<Payment> getByBookingId(UUID bookingId) {
        if (bookingId == null) {
            return Optional.empty();
        }

        var query = em.createQuery(
                "SELECT p FROM PaymentEntity p WHERE p.booking.id = :bookingId",
                PaymentEntity.class
        );
        query.setParameter("bookingId", bookingId);

        return query.getResultStream()
                .findFirst()
                .map(PaymentMapper::toDomain);
    }
}
