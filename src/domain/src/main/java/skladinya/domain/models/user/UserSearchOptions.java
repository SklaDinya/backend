package skladinya.domain.models.user;

public record UserSearchOptions(
        String username,
        String name,
        String email,
        UserRole role,
        int pageSize,
        int pageNumber
) {
}
