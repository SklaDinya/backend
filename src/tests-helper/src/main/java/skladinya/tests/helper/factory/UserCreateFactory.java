package skladinya.tests.helper.factory;

import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserCreate;

public final class UserCreateFactory {

    private UserCreateFactory() {
    }

    public static UserCreate create(User user) {
        return new UserCreate(user.username(), user.password(), user.name(), user.email(), user.role(), user.banned());
    }

    public static UserCreate empty() {
        return new UserCreate(null, null, null, null);
    }
}
