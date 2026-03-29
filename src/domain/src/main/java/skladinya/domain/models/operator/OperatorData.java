package skladinya.domain.models.operator;

import java.util.UUID;

public record OperatorData(
        UUID userId,
        UUID storageId,
        OperatorRole role
) {
}
