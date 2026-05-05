package skladinya.persistence.postgres.repositories;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EntityScan("skladinya.persistence.postgres")
@ComponentScan(basePackages = "skladinya.persistence.postgres")
public class TestApplication {}
