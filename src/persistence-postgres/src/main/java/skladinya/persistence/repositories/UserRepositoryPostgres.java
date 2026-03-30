package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserSearchOptions;
import skladinya.domain.repositories.UserRepository;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryPostgres implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User create(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        UserEntity entity = UserMapper.toEntity(user);

        em.persist(entity);
        em.flush();

        return UserMapper.toDomain(entity);
    }

    @Override
    public Optional<User> getByUserId(UUID userId) {
        if (userId == null) return Optional.empty();

        UserEntity entity = em.find(UserEntity.class, userId);
        return entity != null ? Optional.of(UserMapper.toDomain(entity)) : Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();

        var query = em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.username = :username",
                UserEntity.class
        );
        query.setParameter("username", username);

        return query.getResultStream()
                .findFirst()
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();

        var query = em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.email = :email",
                UserEntity.class
        );
        query.setParameter("email", email);

        return query.getResultStream()
                .findFirst()
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> getAllBySearchOptions(UserSearchOptions options) {
        StringBuilder sb = getStringBuilderQuery(options);

        var query = em.createQuery(sb.toString(), UserEntity.class);

        if (options.username() != null && !options.username().isBlank())
            query.setParameter("username", "%" + options.username() + "%");
        if (options.name() != null && !options.name().isBlank()) query.setParameter("name", "%" + options.name() + "%");
        if (options.email() != null && !options.email().isBlank())
            query.setParameter("email", "%" + options.email() + "%");
        if (options.role() != null) query.setParameter("role", options.role());

        query.setFirstResult(options.pageNumber() * options.pageSize());
        query.setMaxResults(options.pageSize());

        return query.getResultStream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    private static StringBuilder getStringBuilderQuery(UserSearchOptions options) {
        StringBuilder sb = new StringBuilder("SELECT u FROM UserEntity u WHERE 1=1");

        if (options.username() != null && !options.username().isBlank()) sb.append(" AND u.username LIKE :username");
        if (options.name() != null && !options.name().isBlank()) sb.append(" AND u.name LIKE :name");
        if (options.email() != null && !options.email().isBlank()) sb.append(" AND u.email LIKE :email");
        if (options.role() != null) sb.append(" AND u.role = :role");

        sb.append(" ORDER BY u.username ASC");
        return sb;
    }

    @Override
    @Transactional
    public User update(UUID userId, User user) {
        if (userId == null || user == null) return null;

        UserEntity entity = em.find(UserEntity.class, userId);
        if (entity == null) return null;

        entity.setUsername(user.username());
        entity.setPassword(user.password());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setRole(user.role());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        entity.setBanned(user.banned());

        em.merge(entity);
        em.flush();

        return UserMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        if (userId == null) return;

        UserEntity entity = em.find(UserEntity.class, userId);
        if (entity != null) {
            em.remove(entity);
            em.flush();
        }
    }
}
