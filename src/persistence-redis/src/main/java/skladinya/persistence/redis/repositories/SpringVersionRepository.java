package skladinya.persistence.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skladinya.persistence.redis.models.UserVersionModel;

import java.util.UUID;

@Repository
public interface SpringVersionRepository extends CrudRepository<UserVersionModel, UUID> {
}
