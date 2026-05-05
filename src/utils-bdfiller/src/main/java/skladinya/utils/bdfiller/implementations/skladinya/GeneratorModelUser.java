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
@Order(1)
public class GeneratorModelUser extends GeneratorModel<ModelUser> {
    private final PasswordEncoder encoder;

    public GeneratorModelUser(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    private UserRole getRandomRole() {
        double randomNumber = random.nextDouble();

        if (randomNumber < 0.7) {
            return UserRole.Client;
        } else if (randomNumber < 0.9) {
            return UserRole.StorageOperator;
        } else {
            return UserRole.Admin;
        }
    }

    @Override
    public String getModelName() {
        return "users";
    }

    @Override
    public List<ModelUser> generate(Map<String, List<? extends Model>> context) {
        List<ModelUser> out = new ArrayList<>();
        var userCount = 0;
        var operatorCount = 0;
        var adminCount = 0;
        for (int id = 1; id <= baseGeneratedCount; id++) {
            String name = faker.name().fullName();
            var role = getRandomRole();
            String username = switch (role) {
                case UserRole.Client -> String.format("ClientUser%d", ++userCount);
                case UserRole.StorageOperator -> String.format("OperatorUser%d", ++operatorCount);
                case UserRole.Admin -> String.format("AdminUser%d", ++adminCount);
            };
            String email = username + "@yandex.ru";
            if (role != UserRole.StorageOperator) {
                if (random.nextDouble() > 0.3) {
                    email = "";
                }
            }
            LocalDateTime createdDate = LocalDateTime.of(
                    faker.number().numberBetween(2020, 2021),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28),
                    faker.number().numberBetween(0, 23),
                    faker.number().numberBetween(0, 59)
            );
            LocalDateTime updatedDate = createdDate.plusWeeks(faker.number().numberBetween(0, 55));
            boolean banned = Math.random() < 0.05;
            out.add(
                    new ModelUser(
                            UUID.randomUUID(),
                            username,
                            username,
                            name,
                            email,
                            role,
                            createdDate,
                            updatedDate,
                            banned)
            );
        }
        return out;
    }
}
