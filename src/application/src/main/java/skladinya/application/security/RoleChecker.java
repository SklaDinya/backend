package skladinya.application.security;

import org.springframework.stereotype.Component;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.operator.OperatorData;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.user.UserData;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.services.JwtService;

@Component
public class RoleChecker {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public RoleChecker(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UserData authorized(String token) {
        return jwtService.validate(extractToken(token));
    }

    public UserData requireClient(String token) {
        return withRole(token, UserRole.Client);
    }

    public UserData requireAdmin(String token) {
        return withRole(token, UserRole.Admin);
    }

    public OperatorData requireStorageOperator(String token) {
        return jwtService.validateStorageOperator(extractToken(token));
    }

    public OperatorData requireMainStorageOperator(String token) {
        return withRole(token, OperatorRole.MainOperator);
    }

    public OperatorData requireOrdinaryStorageOperator(String token) {
        return withRole(token, OperatorRole.OrdinaryOperator);
    }

    private String extractToken(String token) {
        var prefixSize = BEARER_PREFIX.length();
        return token.length() > prefixSize ? token.substring(prefixSize) : "";
    }

    private UserData withRole(String token, UserRole role) {
        var data = authorized(token);
        if (!role.equals(data.userRole())) {
            throw SklaDinyaException.invalidAccess(
                    String.format("Invalid role (%s expected, %s got)", role, data.userRole()));
        }
        return data;
    }

    private OperatorData withRole(String token, OperatorRole role) {
        var data = requireStorageOperator(token);
        if (!role.equals(data.role())) {
            throw SklaDinyaException.invalidAccess(
                    String.format("Invalid role (%s expected, %s got)", role, data.role()));
        }
        return data;
    }
}
