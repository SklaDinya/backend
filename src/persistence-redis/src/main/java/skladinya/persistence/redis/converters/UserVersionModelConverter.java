package skladinya.persistence.redis.converters;

import skladinya.persistence.redis.models.UserVersionModel;

import java.util.UUID;

public final class UserVersionModelConverter {

    private UserVersionModelConverter() {
    }

    public static UserVersionModel toModel(UUID userId, String version, int ttl) {
        return new UserVersionModel(userId, version, ttl);
    }

    public static String toCoreEntity(UserVersionModel model) {
        return model == null ? "" : model.getVersion();
    }
}
