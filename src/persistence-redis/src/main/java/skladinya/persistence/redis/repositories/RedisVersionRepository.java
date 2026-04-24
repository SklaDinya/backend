package skladinya.persistence.redis.repositories;

import skladinya.domain.repositories.VersionRepository;
import skladinya.persistence.redis.converters.VersionModelConverter;

import java.util.UUID;

public final class RedisVersionRepository implements VersionRepository {

    private final SpringVersionRepository springVersionRepository;

    private final int ttl;

    public RedisVersionRepository(SpringVersionRepository springVersionRepository, int ttl) {
        this.springVersionRepository = springVersionRepository;
        this.ttl = ttl;
    }

    @Override
    public String getByUserId(UUID userId) {
        var result = springVersionRepository.findById(userId).orElse(null);
        return VersionModelConverter.toCoreEntity(result);
    }

    @Override
    public String save(UUID userId, String value) {
        var model = VersionModelConverter.toModel(userId, value, ttl);
        var result = springVersionRepository.save(model);
        return VersionModelConverter.toCoreEntity(result);
    }
}
