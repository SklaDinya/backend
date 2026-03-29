package skladinya.domain.models.user;

public record UserCreate(
        String username,
        String password,
        String name,
        String email,
        UserRole role,
        boolean blocked
) {

    public UserCreate(
            String username,
            String password,
            String name,
            String email,
            boolean blocked
    ) {
        this(
                username,
                password,
                name,
                email,
                UserRole.Client,
                blocked
        );
    }
}
