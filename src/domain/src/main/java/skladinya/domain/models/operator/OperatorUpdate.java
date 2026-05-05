package skladinya.domain.models.operator;

public record OperatorUpdate(
        String username,
        String password,
        String name,
        String email,
        OperatorRole role,
        Boolean banned
) {

    public OperatorUpdate(boolean banned) {
        this(null, null, null, null, null, banned);
    }
}
