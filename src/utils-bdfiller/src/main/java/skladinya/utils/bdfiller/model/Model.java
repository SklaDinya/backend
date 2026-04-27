package skladinya.utils.bdfiller.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class Model {
    public abstract Map<String, Object> toMap();

    public Map<String, Object> toMapByFieldNames(ObjectMapper mapper) {
        return mapper.convertValue(this, new TypeReference<>() {
        });
    }
}
