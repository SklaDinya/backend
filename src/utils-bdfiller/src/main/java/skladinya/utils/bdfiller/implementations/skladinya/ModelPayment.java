package skladinya.utils.bdfiller.implementations.skladinya;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

enum PaymentType {
    NoOp,
    Random
}

interface PaymentPayload {
}

@RequiredArgsConstructor
@Getter
public class ModelPayment extends Model {
    private static final Gson gson = new Gson();
    private final UUID paymentId;
    private final UUID bookingId;
    private final PaymentType paymentType;
    private final PaymentPayload payload;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("payment_id", paymentId.toString());
        map.put("booking_fk", bookingId.toString());
        map.put("payment_type", paymentType.toString());
        map.put("payload", gson.toJson(payload));
        return map;
    }
}

record NoOpPaymentPayload() implements PaymentPayload {
}

record RandomPaymentPayload() implements PaymentPayload {
}
