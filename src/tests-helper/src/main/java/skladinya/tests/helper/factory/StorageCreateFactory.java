package skladinya.tests.helper.factory;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageCreate;

public final class StorageCreateFactory {

    private StorageCreateFactory() {
    }

    public static StorageCreate create(Storage storage, Operator operator) {
        return new StorageCreate(
                OperatorCreateFactory.create(operator),
                storage.name(),
                storage.address(),
                storage.description()
        );
    }
}
