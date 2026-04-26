package skladinya.utils.bdfiller.config;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Random;

@Configuration
public class FakerConfig {

    @Value("${faker.locale:en}")
    private String locale;

    @Value("${faker.seed:0}")
    private long seed;

    @Bean
    public Faker faker() {
        if (seed != 0) {
            return new Faker(Locale.forLanguageTag(locale), new Random(seed));
        }
        return new Faker(Locale.forLanguageTag(locale));
    }
}