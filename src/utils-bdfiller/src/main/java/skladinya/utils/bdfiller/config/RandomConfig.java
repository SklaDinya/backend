package skladinya.utils.bdfiller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfig {
    @Value("${faker.seed:0}")
    private long seed;

    @Bean
    public Random random() {
        return new Random(seed);
    }
}
