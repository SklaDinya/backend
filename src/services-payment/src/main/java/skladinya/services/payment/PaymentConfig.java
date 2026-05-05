package skladinya.services.payment;

public record PaymentConfig(String issuer, String secret, int ttl) {
}
