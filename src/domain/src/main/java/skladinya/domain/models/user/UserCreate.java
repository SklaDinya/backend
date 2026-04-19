package skladinya.domain.models.user;

public record UserCreate(
        String username,
        String password,
        String name,
        String email,
        UserRole role,
        boolean banned
) {

    public UserCreate(
            String username,
            String password,
            String name,
            String email,
            boolean banned
    ) {
        this(
                username,
                password,
                name,
                email,
                UserRole.Client,
                banned
        );
    }
}
