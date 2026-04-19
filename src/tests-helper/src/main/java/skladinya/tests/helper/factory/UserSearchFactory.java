package skladinya.tests.helper.factory;

import skladinya.domain.models.user.UserSearchOptions;

public final class UserSearchFactory {

    private UserSearchFactory() {
    }

    public static UserSearchOptions create() {
        return new UserSearchOptions("", "", "", null, 50, 0);
    }
}
