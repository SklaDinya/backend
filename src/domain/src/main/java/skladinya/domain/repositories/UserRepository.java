package skladinya.domain.repositories;

import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserSearchOptions;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User create(User user);

    Optional<User> getByUserId(UUID userId);

    Optional<User> getByUsername(String username);

    Optional<User> getByEmail(String email);

    Iterable<User> getBySearchOptions(UserSearchOptions options);

    User update(UUID uuid, User user);
}
