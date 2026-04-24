package skladinya.persistence.redis.converters;

import skladinya.persistence.redis.models.VersionModel;

import java.util.UUID;

public final class VersionModelConverter {

    private VersionModelConverter() {
    }

    public static VersionModel toModel(UUID userId, String version, int ttl) {
        return new VersionModel(userId, version, ttl);
    }

    public static String toCoreEntity(VersionModel model) {
        return model == null ? "" : model.getVersion();
    }
}
