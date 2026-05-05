package skladinya.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.operator.OperatorData;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.user.UserData;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.repositories.UserVersionRepository;
import skladinya.domain.services.JwtService;

import java.time.LocalDateTime;
import java.util.*;

public final class JwtServiceImpl implements JwtService {

    private static final String USER_ID_CLAIM = "userId";

    private static final String USER_ROLE_CLAIM = "userRole";

    private static final String STORAGE_ID_CLAIM = "storageId";

    private static final String OPERATOR_ROLE_CLAIM = "operatorRole";

    private static final String VERSION_CLAIM = "version";

    private final UserVersionRepository userVersionRepository;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    private final JWTVerifier operatorVerifier;

    private final String issuer;

    private final int expiration;

    public JwtServiceImpl(UserVersionRepository userVersionRepository, JwtConfig jwtConfig) {
        this.userVersionRepository = userVersionRepository;
        issuer = jwtConfig.issuer();
        algorithm = Algorithm.HMAC256(jwtConfig.secret());
        verifier = createVerifier(UserRole.Client);
        operatorVerifier = createVerifier(UserRole.StorageOperator);
        expiration = jwtConfig.ttl();
    }

    @Override
    public String create(UUID userId, UserRole userRole) {
        return createJwt(userId, userRole, Map.of());
    }

    @Override
    public String createStorageOperator(UUID userId, UUID storageId, OperatorRole operatorRole) {
        return createJwt(userId, UserRole.StorageOperator, Map.of(
                STORAGE_ID_CLAIM, storageId.toString(),
                OPERATOR_ROLE_CLAIM, operatorRole.toString()));
    }

    @Override
    public UserData validate(String token) {
        try {
            var decoded = verifier.verify(token);
            var id = UUID.fromString(decoded.getClaim(USER_ID_CLAIM).asString());
            var role = UserRole.valueOf(decoded.getClaim(USER_ROLE_CLAIM).asString());
            var version = decoded.getClaim(VERSION_CLAIM).asString();
            validateVersion(id, version);
            return new UserData(id, role);
        } catch (JWTVerificationException e) {
            throw SklaDinyaException.badCredentials("Invalid token data");
        }
    }

    @Override
    public OperatorData validateStorageOperator(String token) {
        try {
            var decoded = operatorVerifier.verify(token);
            var id = UUID.fromString(decoded.getClaim(USER_ID_CLAIM).asString());
            var role = UserRole.valueOf(decoded.getClaim(USER_ROLE_CLAIM).asString());
            if (role != UserRole.StorageOperator) {
                throw SklaDinyaException.badCredentials("Invalid token data");
            }
            var storageId = UUID.fromString(decoded.getClaim(STORAGE_ID_CLAIM).asString());
            var operatorRole = OperatorRole.valueOf(decoded.getClaim(OPERATOR_ROLE_CLAIM).asString());
            var version = decoded.getClaim(VERSION_CLAIM).asString();
            validateVersion(id, version);
            return new OperatorData(id, storageId, operatorRole);
        } catch (JWTVerificationException e) {
            throw SklaDinyaException.badCredentials("Invalid token data");
        }
    }

    @Override
    public void update(UUID userId) {
        var version = UUID.randomUUID().toString();
        userVersionRepository.save(userId, version);
    }

    private JWTVerifier createVerifier(UserRole role) {
        var builder = JWT.require(algorithm)
                .withIssuer(issuer)
                .withClaimPresence(USER_ID_CLAIM)
                .withClaimPresence(USER_ROLE_CLAIM);
        if (role == UserRole.StorageOperator) {
            builder = builder.withClaimPresence(STORAGE_ID_CLAIM).withClaimPresence(OPERATOR_ROLE_CLAIM);
        }
        return builder.build();
    }

    private String createJwt(UUID userId, UserRole userRole, Map<String, String> values) {
        if (userId == null || userRole == null) {
            throw SklaDinyaException.validationError("null data while create token");
        }
        var version = userVersionRepository.getByUserId(userId);
        var builder = JWT.create()
                .withIssuer(issuer)
                .withClaim(USER_ID_CLAIM, userId.toString())
                .withClaim(USER_ROLE_CLAIM, userRole.toString());
        for (var pair : values.entrySet()) {
            var key = pair.getKey();
            var value = pair.getValue();
            if (value == null) {
                throw SklaDinyaException.validationError("null data while create token");
            }
            builder = builder.withClaim(key, value);
        }
        return builder
                .withClaim(VERSION_CLAIM, version)
                .withExpiresAt(getExpirationDate(expiration))
                .sign(algorithm);
    }

    private static Date getExpirationDate(int expiration) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, expiration);
        return calendar.getTime();
    }

    private void validateVersion(UUID userId, String version) {
        var expected = userVersionRepository.getByUserId(userId);
        if (!Objects.equals(expected, version)) {
            throw SklaDinyaException.badCredentials("Invalid token version");
        }
    }
}
