package skladinya.tests.helper.factory;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorCreate;

public final class OperatorCreateFactory {

    private OperatorCreateFactory() {
    }

    public static OperatorCreate create(Operator operator) {
        return new OperatorCreate(
                operator.user().username(),
                operator.user().password(),
                operator.user().name(),
                operator.user().email(),
                operator.role()
        );
    }
}
