package skladinya.persistence.repositories;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserSearchOptions;
import skladinya.domain.repositories.UserRepository;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.UserMapper;
import skladinya.persistence.mappers.enums.UserRoleMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringUserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
}

class UserSpecification {

    public static Specification<UserEntity> byOptions(UserSearchOptions options) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (options.username() != null) {
                predicates.add(cb.like(root.get("username"), "%" + options.username() + "%"));
            }

            if (options.name() != null) {
                predicates.add(cb.like(root.get("name"), "%" + options.name() + "%"));
            }

            if (options.email() != null) {
                predicates.add(cb.like(root.get("email"), "%" + options.email() + "%"));
            }

            if (options.role() != null) {
                predicates.add(cb.equal(
                        root.get("role"),
                        UserRoleMapper.toEntity(options.role())
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

@Repository
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepository {

    private final SpringUserRepository repo;

    @Override
    public User create(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = repo.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> getByUserId(UUID userId) {
        return repo.findById(userId)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return repo.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return repo.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> getAllBySearchOptions(UserSearchOptions options) {
        Pageable pageable = PageRequest.of(options.pageNumber(), options.pageSize());

        return repo.findAll(UserSpecification.byOptions(options), pageable)
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public User update(UUID userId, User user) {
        UserEntity existing = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(user.username());
        existing.setPassword(user.password());
        existing.setName(user.name());
        existing.setEmail(user.email());
        existing.setRole(UserRoleMapper.toEntity(user.role()));
        existing.setUpdatedAt(user.updatedAt());
        existing.setBanned(user.banned());

        UserEntity saved = repo.save(existing);
        return UserMapper.toDomain(saved);
    }

    @Override
    public void delete(UUID userId) {
        repo.deleteById(userId);
    }
}
