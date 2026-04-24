package skladinya.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import skladinya.application.configuration.properties.JwtProperties;
import skladinya.domain.repositories.UserVersionRepository;
import skladinya.domain.services.JwtService;
import skladinya.persistence.redis.repositories.RedisUserVersionRepository;
import skladinya.persistence.redis.repositories.SpringUserVersionRepository;
import skladinya.services.jwt.JwtConfig;
import skladinya.services.jwt.JwtServiceImpl;

@Configuration
public class BeanConfig {

    @Bean
    public JwtService jwtService(UserVersionRepository userVersionRepository, JwtProperties props) {
        var config = new JwtConfig(props.issuer(), props.secret(), props.ttl());
        return new JwtServiceImpl(userVersionRepository, config);
    }

    @Bean
    public UserVersionRepository userVersionRepository(SpringUserVersionRepository repository, JwtProperties props) {
        return new RedisUserVersionRepository(repository, props.ttl() * 2);
    }
}
