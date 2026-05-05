package skladinya.persistence.redis.repositories;

import skladinya.domain.repositories.UserVersionRepository;
import skladinya.persistence.redis.converters.UserVersionModelConverter;

import java.util.UUID;

public final class RedisUserVersionRepository implements UserVersionRepository {

    private final SpringUserVersionRepository springUserVersionRepository;

    private final int ttl;

    public RedisUserVersionRepository(SpringUserVersionRepository springUserVersionRepository, int ttl) {
        this.springUserVersionRepository = springUserVersionRepository;
        this.ttl = ttl;
    }

    @Override
    public String getByUserId(UUID userId) {
        var result = springUserVersionRepository.findById(userId).orElse(null);
        return UserVersionModelConverter.toCoreEntity(result);
    }

    @Override
    public String save(UUID userId, String value) {
        var model = UserVersionModelConverter.toModel(userId, value, ttl);
        var result = springUserVersionRepository.save(model);
        return UserVersionModelConverter.toCoreEntity(result);
    }
}
