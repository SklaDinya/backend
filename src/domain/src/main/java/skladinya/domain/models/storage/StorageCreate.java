package skladinya.domain.models.storage;

import skladinya.domain.models.user.UserCreate;

public record StorageCreate(
        String username,
        String password,
        String name,
        String email,
        String storageName,
        String address,
        String description
) {
}
