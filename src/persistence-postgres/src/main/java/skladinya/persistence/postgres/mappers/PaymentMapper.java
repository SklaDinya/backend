package skladinya.persistence.postgres.mappers;

import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentPayload;
import skladinya.persistence.postgres.entities.PaymentEntity;

import com.google.gson.Gson;
import skladinya.persistence.postgres.mappers.enums.PaymentTypeMapper;

public class PaymentMapper {

    private static final Gson gson = new Gson();

    public static PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = payment != null ? new PaymentEntity() : null;

        if (entity != null) {
            entity.setId(payment.paymentId());
            entity.setPaymentType(PaymentTypeMapper.toEntity(payment.paymentType()));
            entity.setPayload(payment.payload() != null ? gson.toJson(payment.payload()) : null);
            entity.setBookingId(payment.bookingId());
        }

        return entity;
    }

    public static Payment toDomain(PaymentEntity entity, Class<? extends PaymentPayload> payloadClass) {
        return entity != null
                ? new Payment(
                entity.getId(),
                entity.getBookingId(),
                PaymentTypeMapper.toDomain(entity.getPaymentType()),
                entity.getPayload() != null ? gson.fromJson(entity.getPayload(), payloadClass) : null
        )
                : null;
    }
}
