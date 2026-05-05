package skladinya.domain.models.storage;

public record StorageUpdate(
        String name,
        String address,
        String description,
        StorageStatus status
) {

    public StorageUpdate(String name, String address, String description) {
        this(name, address, description, null);
    }

    public StorageUpdate(StorageStatus status) {
        this(null, null, null, status);
    }
}
