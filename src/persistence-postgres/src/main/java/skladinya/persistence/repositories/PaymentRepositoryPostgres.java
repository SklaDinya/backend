package skladinya.persistence.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.persistence.entities.PaymentEntity;
import skladinya.persistence.mappers.PaymentMapper;
import skladinya.persistence.mappers.enums.PaymentTypeMapper;
import skladinya.persistence.mappers.helpers.PaymentPayloadResolver;

import java.util.Optional;
import java.util.UUID;

interface SpringPaymentRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByBookingId(UUID bookingId);
}

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryPostgres implements PaymentRepository {

    private final SpringPaymentRepository repo;

    @Override
    public Payment create(Payment payment) {
        PaymentEntity entity = PaymentMapper.toEntity(payment);
        PaymentEntity saved = repo.save(entity);

        return PaymentMapper.toDomain(
                saved,
                PaymentPayloadResolver.resolve(
                        PaymentTypeMapper.toDomain(saved.getPaymentType())
                )
        );
    }

    @Override
    public Optional<Payment> getByBookingId(UUID bookingId) {
        return repo.findByBookingId(bookingId)
                .map(entity -> PaymentMapper.toDomain(
                        entity,
                        PaymentPayloadResolver.resolve(
                                PaymentTypeMapper.toDomain(entity.getPaymentType())
                        )
                ));
    }
}
