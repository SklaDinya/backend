package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModelCell extends Model {
    private final UUID cellId;
    private final UUID storageId;
    private final String name;
    private final String cellClass;
    private final LocalDateTime createdAt;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("cell_id", cellId.toString());
        map.put("name", name);
        map.put("cell_class", cellClass);
        map.put("storage_fk", storageId.toString());
        map.put("created_at", createdAt);
        return map;
    }
}
