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
public class ModelOperator extends Model {
    private final UUID operatorId;
    private final UUID userId;
    private final UUID storageId;
    private final OperatorRole role;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", operatorId.toString());
        map.put("role", role.toString());
        map.put("user_fk", userId.toString());
        map.put("storage_fk", storageId.toString());
        return map;
    }
}

enum OperatorRole {
    MainOperator,
    OrdinaryOperator
}
