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
public class ModelUser extends Model {
    private final UUID userId;
    private final String username;
    private final String password;
    private final String name;
    private final String email;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean banned;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId.toString());
        map.put("username", username);
        map.put("password", password);
        map.put("name", name);
        map.put("email", email);
        map.put("role", role.toString());
        map.put("created_at", createdAt);
        map.put("updated_at", updatedAt);
        map.put("banned", banned);
        return map;
    }
}

enum UserRole {
    Client,
    StorageOperator,
    Admin
}
