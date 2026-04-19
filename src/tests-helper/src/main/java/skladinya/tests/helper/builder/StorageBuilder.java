package skladinya.tests.helper.builder;

import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public final class StorageBuilder {

    private UUID storageId = UUID.randomUUID();
    private String name = UUID.randomUUID().toString();
    private String address = UUID.randomUUID().toString();
    private String description = UUID.randomUUID().toString();
    private StorageStatus status = StorageStatus.Active;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private StorageBuilder() {
    }

    public static StorageBuilder builder() {
        return new StorageBuilder();
    }

    public StorageBuilder storageId(UUID storageId) {
        this.storageId = storageId;
        return this;
    }

    public StorageBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StorageBuilder address(String address) {
        this.address = address;
        return this;
    }

    public StorageBuilder description(String description) {
        this.description = description;
        return this;
    }

    public StorageBuilder status(StorageStatus status) {
        this.status = status;
        return this;
    }

    public StorageBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public StorageBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Storage build() {
        return new Storage(storageId, name, address, description, status, createdAt, updatedAt);
    }
}
