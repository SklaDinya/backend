package skladinya.persistence.postgres.mappers.helpers;

import skladinya.domain.models.payment.PaymentPayload;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;

public class PaymentPayloadResolver {
    public static Class<? extends PaymentPayload> resolve(PaymentType type) {
        if (type == null) return null;

        return switch (type) {
            case NoOp -> NoOpPaymentPayload.class;
            case Random -> RandomPaymentPayload.class;
        };
    }
}
