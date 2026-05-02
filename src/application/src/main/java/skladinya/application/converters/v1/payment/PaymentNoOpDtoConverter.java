package skladinya.application.converters.v1.payment;

import com.google.gson.Gson;
import io.lettuce.core.json.JsonObject;
import skladinya.application.dtos.v1.payment.PaymentNoOpDto;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;

import java.util.UUID;

public final class PaymentNoOpDtoConverter {

    private static final Gson gson = new Gson();

    private PaymentNoOpDtoConverter() {
    }

    public static Payment toCoreEntity(PaymentNoOpDto dto) {
        String receipt = dto.getReceipt();
        UUID paymentId = extractPaymentIdFromReceipt(receipt);
        UUID bookingId = extractBookingIdFromReceipt(receipt);

        return new Payment(
                paymentId,
                bookingId,
                PaymentType.NoOp,
                new NoOpPaymentPayload()
        );
    }

    private static UUID extractPaymentIdFromReceipt(String receipt) {
        try {
            JsonObject json = gson.fromJson(receipt, JsonObject.class);
            return UUID.fromString(json.get("paymentId").asString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract paymentId from receipt", e);
        }
    }

    private static UUID extractBookingIdFromReceipt(String receipt) {
        try {
            JsonObject json = gson.fromJson(receipt, JsonObject.class);
            return UUID.fromString(json.get("bookingId").asString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract bookingId from receipt", e);
        }
    }

}
