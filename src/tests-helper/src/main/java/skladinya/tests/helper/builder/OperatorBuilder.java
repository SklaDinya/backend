package skladinya.tests.helper.builder;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.user.User;

import java.util.UUID;

public final class OperatorBuilder {

    private UUID operatorId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();
    private User user = null;
    private UUID storageId = UUID.randomUUID();
    private OperatorRole role = OperatorRole.OrdinaryOperator;

    private OperatorBuilder() {
    }

    public static OperatorBuilder builder() {
        return new OperatorBuilder();
    }

    public OperatorBuilder operatorId(UUID operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public OperatorBuilder user(User user) {
        this.user = user;
        this.userId = user.userId();
        return this;
    }

    public OperatorBuilder storageId(UUID storageId) {
        this.storageId = storageId;
        return this;
    }

    public OperatorBuilder role(OperatorRole role) {
        this.role = role;
        return this;
    }

    public Operator build() {
        return new Operator(operatorId, userId, user, storageId, role);
    }
}