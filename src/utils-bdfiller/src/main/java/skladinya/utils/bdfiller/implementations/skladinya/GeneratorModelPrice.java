package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Getter
@Order(3)
public class GeneratorModelPrice extends GeneratorModel<ModelPrice> {
    private final PasswordEncoder encoder;

    public GeneratorModelPrice(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    @Override
    public String getModelName() {
        return "prices";
    }

    @Override
    public List<ModelPrice> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> storages = context.getOrDefault("storages", Collections.emptyList());
        List<ModelPrice> out = new ArrayList<>();

        for (Model model : storages) {
            if (!(model instanceof ModelStorage storage)) {
                continue;
            }
            int cntPrice = 3;
            String cellClasses = UtilsFaker.getCellClasses();

            for (int i = 0; i < cntPrice; i++) {
                String cellClass = cellClasses.substring(i, i + 1);
                BigDecimal price = UtilsFaker.getClassPrice(cellClass);
                LocalDateTime createdDate = storage.getCreatedAt().plusDays(faker.number().numberBetween(1, 3));
                out.add(
                        new ModelPrice(
                                UUID.randomUUID(),
                                storage.getStorageId(),
                                price.add(BigDecimal.valueOf((long) faker.number().numberBetween(1, 5) * price.intValue())),
                                cellClass,
                                createdDate
                        )
                );
            }
        }
        return out;
    }
}
