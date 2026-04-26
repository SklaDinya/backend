package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Getter
@Order(6)
public class GeneratorModelBooking extends GeneratorModel<ModelBooking> {
    private final PasswordEncoder encoder;

    public GeneratorModelBooking(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    @Override
    public String getModelName() {
        return "bookings";
    }

    @Override
    public List<ModelBooking> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> cellsCommon = context.getOrDefault("cells", Collections.emptyList());
        List<? extends Model> usersCommon = context.getOrDefault("users", Collections.emptyList());

        List<ModelCell> cells = cellsCommon.stream()
                .filter(c -> c instanceof ModelCell)
                .map(c -> (ModelCell) c)
                .toList();

        List<ModelUser> users = usersCommon.stream()
                .filter(u -> u instanceof ModelUser)
                .map(u -> (ModelUser) u)
                .filter(u -> u.getRole() == UserRole.Client)
                .toList();

        List<ModelBooking> out = new ArrayList<>();

        for (int id = 1; id <= 2 * baseGeneratedCount; id++) {

            LocalDateTime createdDate = LocalDateTime.of(
                    faker.number().numberBetween(2022, 2025),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28),
                    faker.number().numberBetween(0, 23),
                    faker.number().numberBetween(0, 59)
            );
            LocalDateTime startTime = createdDate.plusHours(faker.number().numberBetween(1, 10));
            Duration bookingTime = Duration.ofHours(faker.number().numberBetween(1, 100));
            ModelCell cell = getRandomElement(cells);

            String cellClass = cell.getCellClass();
            BigDecimal price = UtilsFaker.getClassPrice(cellClass);

                    out.add(
                    new ModelBooking(
                            UUID.randomUUID(),
                            getRandomElement(users).getUserId(),
                            cell.getStorageId(),
                            List.of(cell.getCellId()),
                            startTime,
                            bookingTime,
                            startTime.plus(bookingTime),
                            createdDate,
                            BookingStatus.Finished,
                            BigDecimal.valueOf(price.intValue() * bookingTime.toHours())
                    )
            );
        }
        return out;
    }
}
