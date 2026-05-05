package skladinya.domain.models.storage;

import skladinya.domain.models.operator.OperatorCreate;

public record StorageCreate(
        OperatorCreate operatorCreate,
        String storageName,
        String address,
        String description
) {
}
