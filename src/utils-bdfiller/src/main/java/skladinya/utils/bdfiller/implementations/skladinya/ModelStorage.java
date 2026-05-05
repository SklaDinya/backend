package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModelStorage extends Model {
    private final UUID storageId;
    private final String name;
    private final String address;
    private final String description;
    private final StorageStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("storage_id", storageId.toString());
        map.put("name", name);
        map.put("address", address);
        map.put("description", description);
        map.put("status", status.toString());
        map.put("created_at", createdAt);
        map.put("updated_at", updatedAt);
        return map;
    }
}

enum StorageStatus {
    Created,
    Active
}
