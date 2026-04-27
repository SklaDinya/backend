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
@Order(5)
public class GeneratorModelOperator extends GeneratorModel<ModelOperator> {
    private final PasswordEncoder encoder;

    public GeneratorModelOperator(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    private OperatorRole getRandomRole() {
        if (random.nextDouble() < 0.8) {
            return OperatorRole.OrdinaryOperator;
        } else {
            return OperatorRole.MainOperator;
        }
    }

    @Override
    public String getModelName() {
        return "operators";
    }

    @Override
    public List<ModelOperator> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> storages = context.getOrDefault("storages", Collections.emptyList());
        List<? extends Model> users = context.getOrDefault("users", Collections.emptyList());
        List<ModelOperator> out = new ArrayList<>();

        List<ModelUser> operators = users.stream()
                .filter(u -> u instanceof ModelUser)
                .map(u -> (ModelUser) u)
                .filter(u -> u.getRole() == UserRole.StorageOperator)
                .toList();
        int operatorIndex = 0;

        for (Model model : storages) {
            if (!(model instanceof ModelStorage storage)) {
                continue;
            }
            out.add(
                    new ModelOperator(
                            UUID.randomUUID(),
                            operators.get(operatorIndex).getUserId(),
                            storage.getStorageId(),
                            OperatorRole.MainOperator
                    )
            );
            operatorIndex = (operatorIndex + 1) % operators.size();
        }

        for (int i = operatorIndex; i < operators.size(); i++) {
            out.add(
                    new ModelOperator(
                            UUID.randomUUID(),
                            operators.get(i).getUserId(),
                            UtilsFaker.getRandomElement(out).getStorageId(),
                            OperatorRole.OrdinaryOperator
                    )
            );
        }
        return out;
    }
}
