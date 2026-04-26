package skladinya.utils.bdfiller.implementations.illustration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelExample extends Model {
    private final int anyInt;
    private final String anyString;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Number", anyInt);
        map.put("Raw string", anyString);
        return map;
    }
}
