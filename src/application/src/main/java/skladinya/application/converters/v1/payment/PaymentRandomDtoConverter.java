package skladinya.application.converters.v1.payment;

import skladinya.application.dtos.v1.payment.PaymentRandomDto;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;

import java.util.UUID;

public final class PaymentRandomDtoConverter {

    private PaymentRandomDtoConverter() {
    }

    public static Payment toCoreEntity(PaymentRandomDto dto, UUID bookingId) {
        return new Payment(
                UUID.randomUUID(),
                bookingId,
                PaymentType.Random,
                new RandomPaymentPayload()
        );
    }

}
