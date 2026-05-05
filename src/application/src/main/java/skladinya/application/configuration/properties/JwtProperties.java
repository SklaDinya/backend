package skladinya.application.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "skladinya.jwt")
public record JwtProperties(String issuer, String secret, int ttl) {
}
