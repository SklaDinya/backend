package skladinya.tests.helper.factory;

import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;

import java.util.UUID;

public final class PaymentFactory {

    private PaymentFactory() {
    }

    public static Payment createNoOp(UUID bookingId) {
        return new Payment(
                UUID.randomUUID(),
                bookingId,
                PaymentType.NoOp,
                new NoOpPaymentPayload()
        );
    }

    public static Payment createRandom(UUID bookingId) {
        return new Payment(
                UUID.randomUUID(),
                bookingId,
                PaymentType.Random,
                new RandomPaymentPayload()
        );
    }
}
