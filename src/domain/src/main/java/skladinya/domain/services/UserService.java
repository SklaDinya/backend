package skladinya.domain.services;

import skladinya.domain.models.user.*;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(UserCreate createForm);

    User get(UUID userId);

    List<User> getBySearchOptions(UserSearchOptions options);

    User update(UUID userId, UserUpdate updateForm);

    String updateSelf(UUID userId, SelfUpdate updateForm);
}
