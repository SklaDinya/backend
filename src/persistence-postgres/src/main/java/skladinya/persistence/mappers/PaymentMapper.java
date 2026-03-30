package skladinya.persistence.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentPayload;
import skladinya.persistence.entities.BookingEntity;
import skladinya.persistence.entities.PaymentEntity;

import java.util.UUID;

public class PaymentMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PaymentEntity toEntity(Payment payment, BookingEntity booking) {
        if (payment == null) {
            return null;
        }

        String payloadJson = serializePayload(payment.payload());

        PaymentEntity entity = new PaymentEntity(
                payment.paymentId(),
                payment.paymentType(),
                payloadJson,
                booking
        );

        entity.setBooking(booking);

        return entity;
    }

    public static Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        UUID bookingId = entity.getBooking() != null
                ? entity.getBooking().getId()
                : null;

        PaymentPayload payload = deserializePayload(entity.getPayload());

        return new Payment(
                entity.getId(),
                bookingId,
                entity.getPaymentType(),
                payload
        );
    }

    private static String serializePayload(PaymentPayload payload) {
        try {
            return payload != null
                    ? objectMapper.writeValueAsString(payload)
                    : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize PaymentPayload", e);
        }
    }

    private static PaymentPayload deserializePayload(String json) {
        try {
            return json != null
                    ? objectMapper.readValue(json, PaymentPayload.class)
                    : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize PaymentPayload", e);
        }
    }
}
