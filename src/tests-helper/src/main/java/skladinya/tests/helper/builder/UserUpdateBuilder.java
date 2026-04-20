package skladinya.tests.helper.builder;

import skladinya.domain.models.user.SelfUpdate;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.models.user.UserUpdate;

import java.util.UUID;

public final class UserUpdateBuilder {
    private String username;
    private String password;
    private String name;
    private String email;
    private UserRole role;
    private Boolean banned;

    private UserUpdateBuilder() {}

    public static UserUpdateBuilder builder() {
        return new UserUpdateBuilder();
    }

    public UserUpdateBuilder random() {
        this.username = UUID.randomUUID().toString();
        this.password = UUID.randomUUID().toString();
        this.name = UUID.randomUUID().toString();
        this.email = UUID.randomUUID().toString() + "@a.b";
        this.role = UserRole.Client;
        this.banned = false;
        return this;
    }

    public UserUpdateBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserUpdateBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserUpdateBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserUpdateBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserUpdateBuilder role(UserRole role) {
        this.role = role;
        return this;
    }

    public UserUpdateBuilder banned(Boolean banned) {
        this.banned = banned;
        return this;
    }

    public UserUpdate build() {
        return new UserUpdate(username, password, name, email, role, banned);
    }

    public SelfUpdate buildSelf(String old) {
        return new SelfUpdate(username, old, password, name, email);
    }
}
