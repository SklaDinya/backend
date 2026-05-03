package skladinya.services.payment;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.domain.services.PaymentService;
import skladinya.services.payment.adapters.DurationAdapter;
import skladinya.services.payment.adapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentServiceImpl implements PaymentService {

    private static final String BOOKING_CLAIM = "booking";

    private final PaymentRepository paymentRepository;

    private final Synchronizer synchronizer;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    private final String issuer;

    private final int expiration;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            PaymentConfig config,
            Synchronizer synchronizer
    ) {
        this.paymentRepository = paymentRepository;
        this.synchronizer = synchronizer;
        issuer = config.issuer();
        algorithm = Algorithm.HMAC256(config.secret());
        verifier = createVerifier();
        expiration = config.ttl();
    }

    @Override
    public String createReceipt(Booking booking) {
        var gson = getGson();
        var data = gson.toJson(booking);
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(BOOKING_CLAIM, data)
                .withExpiresAt(getExpirationDate(expiration))
                .sign(algorithm);
    }

    @Override
    public Booking validateReceipt(String receipt) {
        return synchronizer.executeSingleFunction(() -> {
            try {
                var decoded = verifier.verify(receipt);
                var data = decoded.getClaim(BOOKING_CLAIM).asString();
                var gson = getGson();
                return gson.fromJson(data, Booking.class);
            } catch (JWTVerificationException e) {
                throw SklaDinyaException.validationError("Invalid receipt data");
            }
        });
    }

    @Override
    public Payment payNoOp(Booking booking) {
        return synchronizer.executeTransactionFunction(() -> {

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            var payment = new Payment(
                    UUID.randomUUID(),
                    booking.bookingId(),
                    PaymentType.NoOp,
                    new NoOpPaymentPayload()
            );

            return paymentRepository.create(payment);
        });
    }

    @Override
    public Payment payRandom(Booking booking) {
        return synchronizer.executeTransactionFunction(() -> {

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            if (ThreadLocalRandom.current().nextBoolean()) {
                throw SklaDinyaException.joke("Random payment failed");
            }

            var payment = new Payment(
                    UUID.randomUUID(),
                    booking.bookingId(),
                    PaymentType.Random,
                    new RandomPaymentPayload()
            );

            return paymentRepository.create(payment);
        });
    }

    @Override
    public void cancel(Payment payment) {
        synchronizer.executeTransactionConsumer(() -> {

            if (payment == null) {
                throw SklaDinyaException.validationError("Payment is null and WTF");
            }
        });
    }

    private static Date getExpirationDate(int expiration) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiration);
        return calendar.getTime();
    }

    private JWTVerifier createVerifier() {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .withClaimPresence(BOOKING_CLAIM)
                .build();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
