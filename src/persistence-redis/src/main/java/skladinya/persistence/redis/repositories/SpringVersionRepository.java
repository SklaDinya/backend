package skladinya.persistence.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skladinya.persistence.redis.models.VersionModel;

import java.util.UUID;

@Repository
public interface SpringVersionRepository extends CrudRepository<VersionModel, UUID> {
}
