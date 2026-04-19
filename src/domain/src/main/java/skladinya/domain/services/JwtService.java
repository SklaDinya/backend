package skladinya.domain.services;

import skladinya.domain.models.operator.OperatorData;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.user.UserData;
import skladinya.domain.models.user.UserRole;

import java.util.UUID;

public interface JwtService {

    String create(UUID userId, UserRole userRole);

    String createStorageOperator(UUID userId, UserRole userRole, UUID storageId, OperatorRole operatorRole);

    UserData validate(String token);

    OperatorData validateStorageOperator(String token);

    void update(UUID userId);
}
