package skladinya.domain.models.operator;

public record OperatorSearchOptions(
        String username,
        String name,
        String email,
        OperatorRole role,
        int pageSize,
        int pageNumber
) {

    public OperatorSearchOptions(int pageSize, int pageNumber) {
        this(null, null, null, null, pageSize, pageNumber);
    }
}
