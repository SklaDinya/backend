package skladinya.domain.models.storage;

import java.util.List;

public record StorageSearchOptions(
        String name,
        String address,
        List<StorageStatus> statuses,
        int pageSize,
        int pageNumber
) {
}
