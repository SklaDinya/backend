package skladinya.tests.helper.factory;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageUpdate;
import skladinya.tests.helper.builder.StorageBuilder;

public final class StorageUpdateFactory {

    private StorageUpdateFactory() {
    }

    public static Storage create(Storage existingStorage, StorageUpdate update) {
        return StorageBuilder.builder()
                .storageId(existingStorage.storageId())
                .name(update.name() == null ? existingStorage.name() : update.name())
                .address(update.address() == null ? existingStorage.address() : update.address())
                .description(update.description() == null ? existingStorage.description() : update.description())
                .createdAt(existingStorage.createdAt())
                .build();
    }
}
