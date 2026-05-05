package skladinya.domain.models.user;

public record SelfUpdate(
        String username,
        String oldPassword,
        String password,
        String name,
        String email
) {
}
