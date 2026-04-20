package skladinya.tests.helper.factory;

import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserUpdate;
import skladinya.tests.helper.builder.UserBuilder;

public final class UserUpdateFactory {

    private  UserUpdateFactory() {
    }

    public static User create(User existingUser, UserUpdate update) {
        return UserBuilder.builder()
                .userId(existingUser.userId())
                .username(update.username() != null ? update.username() : existingUser.username())
                .password(update.password() != null ? update.password() : existingUser.password())
                .name(update.name() != null ? update.name() : existingUser.name())
                .email(update.email() != null ? update.email() : existingUser.email())
                .role(update.role() != null ? update.role() : existingUser.role())
                .banned(update.banned() != null ? update.banned() : existingUser.banned())
                .createdAt(existingUser.createdAt())
                .build();
    }
}
