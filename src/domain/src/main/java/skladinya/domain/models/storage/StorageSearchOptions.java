package skladinya.domain.models.storage;

public record StorageSearchOptions(
        String name,
        String address,
        StorageStatus status,
        int pageSize,
        int pageNumber
) {

    public StorageSearchOptions(String name, String address, int pageSize, int pageNumber) {
        this(name, address, StorageStatus.Active, pageSize, pageNumber);
    }

    public StorageSearchOptions(int pageSize, int pageNumber) {
        this(null, null, StorageStatus.Created, pageSize, pageNumber);
    }
}
