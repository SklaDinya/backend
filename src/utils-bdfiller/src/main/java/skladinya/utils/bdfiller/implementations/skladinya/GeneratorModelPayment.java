package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Getter
@Order(7)
public class GeneratorModelPayment extends GeneratorModel<ModelPayment> {
    private final PasswordEncoder encoder;

    public GeneratorModelPayment(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    private static PaymentType getRandomPaymentType() {
        double random = ThreadLocalRandom.current().nextDouble();

        if (random < 0.5) {
            return PaymentType.Random;
        } else {
            return PaymentType.NoOp;
        }
    }

    private static PaymentPayload getPayload(PaymentType type) {
        return switch (type) {
            case PaymentType.NoOp -> new NoOpPaymentPayload();
            case PaymentType.Random -> new RandomPaymentPayload();
        };
    }

    @Override
    public String getModelName() {
        return "payments";
    }

    @Override
    public List<ModelPayment> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> bookings = context.getOrDefault("bookings", Collections.emptyList());
        List<ModelPayment> out = new ArrayList<>();

        for (Model model : bookings) {
            if (!(model instanceof ModelBooking booking)) {
                continue;
            }

            PaymentType paymentType = getRandomPaymentType();
            out.add(
                    new ModelPayment(
                            UUID.randomUUID(),
                            booking.getBookingId(),
                            paymentType,
                            getPayload(paymentType)
                    )
            );
        }
        return out;
    }
}
