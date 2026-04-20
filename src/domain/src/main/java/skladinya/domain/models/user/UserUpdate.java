package skladinya.domain.models.user;

public record UserUpdate(
        String username,
        String password,
        String name,
        String email,
        UserRole role,
        Boolean banned
) {

    public UserUpdate(SelfUpdate selfUpdate) {
        this(
                selfUpdate.username(),
                selfUpdate.password(),
                selfUpdate.name(),
                selfUpdate.email(),
                null,
                null
        );
    }
}
