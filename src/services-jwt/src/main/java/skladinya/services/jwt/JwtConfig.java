package skladinya.services.jwt;

public record JwtConfig(String issuer, String secret, int ttl) {
}
