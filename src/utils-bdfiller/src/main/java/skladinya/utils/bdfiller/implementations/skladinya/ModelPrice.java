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
public class ModelPrice extends Model {
    private final UUID priceId;
    private final UUID storageId;
    private final BigDecimal price;
    private final String cellClass;
    private final LocalDateTime createdAt;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("price_id", priceId.toString());
        map.put("cell_class", cellClass);
        map.put("price", price);
        map.put("storage_fk", storageId.toString());
        map.put("created_at", createdAt);
        return map;
    }
}
