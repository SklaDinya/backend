package skladinya.utils.bdfiller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GeneratorImplementationsConfig {
    @Value("${config.generators.package:org.srd.fakeDataGenerator.implementations.illustration}")
    private String packageName;

    @Bean
    public List<GeneratorModel<? extends Model>> generators(ApplicationContext ctx) {
        List<GeneratorModel<? extends Model>> generatorList = new ArrayList<>();
        ctx.getBeansOfType(GeneratorModel.class).values().forEach(g -> {
            if (g.getClass().getPackageName().startsWith(packageName)) {
                generatorList.add((GeneratorModel<? extends Model>) g);
            }
        });

        generatorList.sort(AnnotationAwareOrderComparator.INSTANCE);
        return generatorList;
    }
}
