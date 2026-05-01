package skladinya.domain.models.operator;

import skladinya.domain.models.user.User;

import java.util.UUID;

public record Operator(
        UUID operatorId,
        UUID userId,
        User user,
        UUID storageId,
        OperatorRole role
) {

    public Operator(User user, UUID storageId, OperatorRole role) {
        this(UUID.randomUUID(), user.userId(), user, storageId, role);
    }
}
