package skladinya.domain.models.operator;

public record OperatorUpdate(
        String username,
        String password,
        String name,
        String email,
        OperatorRole role,
        Boolean banned
) {
}
