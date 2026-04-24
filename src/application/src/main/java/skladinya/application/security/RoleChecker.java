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

    private final JwtService jwtService;

    public RoleChecker(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UserData authorized(String token) {
        return jwtService.validate(extractToken(token));
    }

    public UserData client(String token) {
        return withRole(token, UserRole.Client);
    }

    public UserData admin(String token) {
        return withRole(token, UserRole.Admin);
    }

    public OperatorData storageOperator(String token) {
        return jwtService.validateStorageOperator(extractToken(token));
    }

    public OperatorData mainStorageOperator(String token) {
        return withRole(token, OperatorRole.MainOperator);
    }

    public OperatorData ordinaryStorageOperator(String token) {
        return withRole(token, OperatorRole.OrdinaryOperator);
    }

    private String extractToken(String token) {
        return token.substring(7);
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
        var data = storageOperator(token);
        if (!role.equals(data.role())) {
            throw SklaDinyaException.invalidAccess(
                    String.format("Invalid role (%s expected, %s got)", role, data.role()));
        }
        return data;
    }
}
