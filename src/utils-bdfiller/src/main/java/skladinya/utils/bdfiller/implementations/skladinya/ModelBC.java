package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ModelBC extends Model {
    private final UUID bookingId;
    private final UUID cellId;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("booking_fk", bookingId.toString());
        map.put("cell_fk", cellId.toString());
        return map;
    }
}
