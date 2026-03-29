package skladinya.domain.models.operator;

public record OperatorCreate(
        String username,
        String password,
        String name,
        String email,
        OperatorRole role
) {
}
