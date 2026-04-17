package skladinya.tests.helper.builder;

import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public final class UserBuilder {

    private UUID userId = UUID.randomUUID();
    private String username = "random_username";
    private String password = "qwerty123";
    private String name = "Зубенко Михаил Петрович";
    private String email = "abc@d.e";
    private UserRole role = UserRole.Client;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean banned = false;

    private UserBuilder() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public UserBuilder userId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder role(UserRole role) {
        this.role = role;
        return this;
    }

    public UserBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UserBuilder banned(boolean banned) {
        this.banned = banned;
        return this;
    }

    public User build() {
        return new User(userId, username, password, name, email, role, createdAt, updatedAt, banned);
    }
}
