package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.persistence.entities.OperatorEntity;
import skladinya.persistence.entities.StorageEntity;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.OperatorMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OperatorRepositoryPostgres implements OperatorRepository {

    @PersistenceContext
    private EntityManager em;

    private static StringBuilder getStringBuilderQuery(OperatorSearchOptions options) {
        StringBuilder sb = new StringBuilder("SELECT o FROM OperatorEntity o WHERE 1=1");

        if (options.username() != null && !options.username().isBlank())
            sb.append(" AND o.user.username LIKE :username");
        if (options.name() != null && !options.name().isBlank()) sb.append(" AND o.user.name LIKE :name");
        if (options.email() != null && !options.email().isBlank()) sb.append(" AND o.user.email LIKE :email");
        if (options.role() != null) sb.append(" AND o.role = :role");

        sb.append(" ORDER BY o.user.username ASC");
        return sb;
    }

    @Override
    @Transactional
    public Operator create(Operator operator) {
        if (operator == null) throw new IllegalArgumentException("Operator cannot be null");

        UserEntity user = em.find(UserEntity.class, operator.userId());
        StorageEntity storage = em.find(StorageEntity.class, operator.storageId());

        OperatorEntity entity = OperatorMapper.toEntity(operator, user, storage);

        em.persist(entity);
        em.flush();

        return OperatorMapper.toDomain(entity);
    }

    @Override
    public Optional<Operator> getByOperatorId(UUID operatorId) {
        if (operatorId == null) return Optional.empty();

        var query = em.createQuery(
                "SELECT o FROM OperatorEntity o WHERE o.id = :id",
                OperatorEntity.class
        );
        query.setParameter("id", operatorId);

        return query.getResultStream()
                .findFirst()
                .map(OperatorMapper::toDomain);
    }

    @Override
    public Optional<Operator> getByUserId(UUID userId) {
        if (userId == null) return Optional.empty();

        var query = em.createQuery(
                "SELECT o FROM OperatorEntity o WHERE o.user.id = :userId",
                OperatorEntity.class
        );
        query.setParameter("userId", userId);

        return query.getResultStream()
                .findFirst()
                .map(OperatorMapper::toDomain);
    }

    @Override
    public List<Operator> getAllBySearchOptions(OperatorSearchOptions options) {
        StringBuilder sb = getStringBuilderQuery(options);

        var query = em.createQuery(sb.toString(), OperatorEntity.class);

        if (options.username() != null && !options.username().isBlank())
            query.setParameter("username", "%" + options.username() + "%");
        if (options.name() != null && !options.name().isBlank()) query.setParameter("name", "%" + options.name() + "%");
        if (options.email() != null && !options.email().isBlank())
            query.setParameter("email", "%" + options.email() + "%");
        if (options.role() != null) query.setParameter("role", options.role());

        query.setFirstResult(options.pageNumber() * options.pageSize());
        query.setMaxResults(options.pageSize());

        return query.getResultStream()
                .map(OperatorMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Operator update(UUID operatorId, Operator operator) {
        if (operatorId == null || operator == null) return null;

        OperatorEntity entity = em.find(OperatorEntity.class, operatorId);
        if (entity == null) return null;

        entity.setRole(operator.role());

        if (operator.userId() != null) {
            UserEntity user = em.find(UserEntity.class, operator.userId());
            entity.setUser(user);
        }
        if (operator.storageId() != null) {
            StorageEntity storage = em.find(StorageEntity.class, operator.storageId());
            entity.setStorage(storage);
        }

        em.merge(entity);
        em.flush();

        return OperatorMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(UUID operatorId) {
        if (operatorId == null) return;

        OperatorEntity entity = em.find(OperatorEntity.class, operatorId);
        if (entity != null) {
            em.remove(entity);
            em.flush();
        }
    }
}
