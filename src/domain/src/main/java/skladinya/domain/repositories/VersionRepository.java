package skladinya.domain.repositories;

import java.util.UUID;

public interface VersionRepository {

    String getByUserId(UUID userId);

    String save(UUID userId, String value);
}
