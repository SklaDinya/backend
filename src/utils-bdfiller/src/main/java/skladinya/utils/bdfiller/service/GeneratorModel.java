package skladinya.utils.bdfiller.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public abstract class GeneratorModel<T extends Model> {
    @Value("${config.models.baseGeneratedNumber: 2}")
    protected int baseGeneratedCount;
    protected final Faker faker;
    protected final Random random;

    public abstract String getModelName();
    public abstract List<T> generate(Map<String, List<? extends Model>> context);
}
