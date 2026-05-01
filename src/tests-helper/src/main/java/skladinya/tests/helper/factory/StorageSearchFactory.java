package skladinya.tests.helper.factory;

import skladinya.domain.models.storage.StorageSearchOptions;

import java.util.List;

public final class StorageSearchFactory {

    private StorageSearchFactory() {
    }

    public static StorageSearchOptions create() {
        return new StorageSearchOptions(null, null, List.of(), 50, 0);
    }
}
