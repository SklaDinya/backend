package skladinya.domain.models.user;

import java.util.UUID;

public record UserData(
        UUID userId,
        UserRole userRole
) {
}
