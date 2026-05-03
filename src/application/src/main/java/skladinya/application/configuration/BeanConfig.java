package skladinya.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.application.configuration.properties.JwtProperties;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.domain.repositories.UserVersionRepository;
import skladinya.domain.services.JwtService;
import skladinya.domain.services.PaymentService;
import skladinya.persistence.redis.repositories.RedisUserVersionRepository;
import skladinya.persistence.redis.repositories.SpringUserVersionRepository;
import skladinya.services.jwt.JwtConfig;
import skladinya.services.jwt.JwtServiceImpl;
import skladinya.services.payment.PaymentConfig;
import skladinya.services.payment.PaymentServiceImpl;

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

    @Bean
    public PaymentConfig paymentConfig(
            JwtProperties jwtProperties,
            @Value("${skladinya.booking.payment-timeout-minutes}") int ttl
    ) {
        return new PaymentConfig(
                jwtProperties.issuer(),
                jwtProperties.secret(),
                ttl
        );
    }

    @Bean
    @RequestScope
    public PaymentService paymentService(
            PaymentRepository paymentRepository,
            PaymentConfig cfg,
            Synchronizer synchronizer
    ) {
        return new PaymentServiceImpl(paymentRepository, cfg, synchronizer);
    }
}
