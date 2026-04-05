package skladinya.persistence.repositories;

import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserSearchOptions;
import skladinya.domain.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryPostgres implements UserRepository {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public Optional<User> getByUserId(UUID userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllBySearchOptions(UserSearchOptions options) {
        return List.of();
    }

    @Override
    public User update(UUID userId, User user) {
        return null;
    }

    @Override
    public void delete(UUID userId) {

    }
}
