package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.util.*;

@Service
@Getter
@Order(8)
public class GeneratorModelBC extends GeneratorModel<ModelBC> {
    private final PasswordEncoder encoder;

    public GeneratorModelBC(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    @Override
    public String getModelName() {
        return "booking_cells";
    }

    @Override
    public List<ModelBC> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> bookings = context.getOrDefault("bookings", Collections.emptyList());
        List<ModelBC> out = new ArrayList<>();

        for (Model model : bookings) {
            if (!(model instanceof ModelBooking booking)) {
                continue;
            }

            out.add(
                    new ModelBC(
                            booking.getBookingId(),
                            booking.getCellIds().getFirst()
                    )
            );
        }
        return out;
    }
}
