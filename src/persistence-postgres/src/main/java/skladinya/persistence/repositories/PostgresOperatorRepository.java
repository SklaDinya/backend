package skladinya.persistence.repositories;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.persistence.entities.OperatorEntity;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.OperatorMapper;
import skladinya.persistence.mappers.enums.OperatorRoleMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringOperatorRepository extends
        JpaRepository<OperatorEntity, UUID>,
        JpaSpecificationExecutor<OperatorEntity> {

    Optional<OperatorEntity> findByUserId(UUID userId);
}

class OperatorSpecification {

    public static Specification<OperatorEntity> byOptions(OperatorSearchOptions options) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<OperatorEntity, UserEntity> userJoin = root.join("user", JoinType.LEFT);

            if (options.username() != null && !options.username().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(userJoin.get("username")),
                        "%" + options.username().toLowerCase() + "%"
                ));
            }

            if (options.name() != null && !options.name().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(userJoin.get("name")),
                        "%" + options.name().toLowerCase() + "%"
                ));
            }

            if (options.email() != null && !options.email().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(userJoin.get("email")),
                        "%" + options.email().toLowerCase() + "%"
                ));
            }

            if (options.role() != null) {
                predicates.add(cb.equal(
                        root.get("role"),
                        OperatorRoleMapper.toEntity(options.role())
                ));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

@Repository
@RequiredArgsConstructor
public class PostgresOperatorRepository implements OperatorRepository {

    private final SpringOperatorRepository repo;

    @Override
    public Operator create(Operator operator) {
        OperatorEntity entity = OperatorMapper.toEntity(operator);
        OperatorEntity saved = repo.save(entity);
        return OperatorMapper.toDomain(saved);
    }

    @Override
    public Optional<Operator> getByOperatorId(UUID operatorId) {
        return repo.findById(operatorId)
                .map(OperatorMapper::toDomain);
    }

    @Override
    public Optional<Operator> getByUserId(UUID userId) {
        return repo.findByUserId(userId)
                .map(OperatorMapper::toDomain);
    }

    @Override
    public List<Operator> getAllBySearchOptions(OperatorSearchOptions options) {
        Pageable pageable = PageRequest.of(options.pageNumber(), options.pageSize());

        return repo.findAll(OperatorSpecification.byOptions(options), pageable)
                .stream()
                .map(OperatorMapper::toDomain)
                .toList();
    }

    @Override
    public Operator update(UUID operatorId, Operator operator) {
        OperatorEntity existing = repo.findById(operatorId)
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        existing.setRole(OperatorRoleMapper.toEntity(operator.role()));
        existing.setUserId(operator.userId());
        existing.setStorageId(operator.storageId());

        OperatorEntity saved = repo.save(existing);
        return OperatorMapper.toDomain(saved);
    }

    @Override
    public void delete(UUID operatorId) {
        repo.deleteById(operatorId);
    }
}
