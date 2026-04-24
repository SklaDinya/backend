package skladinya.domain.repositories;

import java.util.UUID;

public interface UserVersionRepository {

    String getByUserId(UUID userId);

    String save(UUID userId, String value);
}
