package skladinya.domain.models.payment;

import java.util.UUID;

public record Payment(
        UUID paymentId,
        UUID bookingId,
        PaymentType paymentType,
        PaymentPayload payload
) {
}
