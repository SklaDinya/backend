package skladinya.domain.models.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
        UUID userId,
        String username,
        String password,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean banned
) {

    public User(
            UUID userId,
            String username,
            String password,
            String name,
            String email,
            UserRole role,
            LocalDateTime createdAt,
            boolean banned
    ) {
        this(
                userId,
                username,
                password,
                name,
                email,
                role,
                createdAt,
                LocalDateTime.now(),
                banned
        );
    }

    public User(

            String username,
            String password,
            String name,
            String email,
            UserRole role,
            boolean banned
    ) {
        this(
                UUID.randomUUID(),
                username,
                password,
                name,
                email,
                role,
                LocalDateTime.now(),
                banned
        );
    }
}
