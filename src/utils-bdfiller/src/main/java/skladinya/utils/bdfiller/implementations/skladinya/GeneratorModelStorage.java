package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Getter
@Order(2)
public class GeneratorModelStorage extends GeneratorModel<ModelStorage> {
    private final PasswordEncoder encoder;

    public GeneratorModelStorage(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    private StorageStatus getRandomStatus() {
        if (random.nextDouble() < 0.8) {
            return StorageStatus.Active;
        } else {
            return StorageStatus.Created;
        }
    }

    @Override
    public String getModelName() {
        return "storages";
    }

    @Override
    public List<ModelStorage> generate(Map<String, List<? extends Model>> context) {
        List<ModelStorage> out = new ArrayList<>();
        List<? extends Model> users = context.getOrDefault("users", Collections.emptyList());
        var generated = baseGeneratedCount / 4 + 1;
        var found = users.stream().filter(u -> ((ModelUser)u).getRole().equals(UserRole.StorageOperator)).count();
        var count = Math.min(generated, found);
        for (int id = 1; id < count; id++) {
            String name = faker.lorem().word();
            String address = "Улица " + faker.name().lastName();
            String description = UtilsFaker.generateSentence(2, 5);
            LocalDateTime createdDate = LocalDateTime.of(
                    faker.number().numberBetween(2017, 2019),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28),
                    faker.number().numberBetween(0, 23),
                    faker.number().numberBetween(0, 59)
            );
            LocalDateTime updatedDate = createdDate.plusWeeks(faker.number().numberBetween(0, 55));
            out.add(
                    new ModelStorage(
                            UUID.randomUUID(),
                            name,
                            address,
                            description,
                            StorageStatus.Active,
                            createdDate,
                            updatedDate)
            );
        }
        return out;
    }
}
