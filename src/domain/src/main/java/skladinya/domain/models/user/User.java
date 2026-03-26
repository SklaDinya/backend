package skladinya.domain.models.user;

import java.util.Date;
import java.util.UUID;

public record User(
        UUID userId,
        String username,
        String password,
        String name,
        String email,
        UserRole role,
        Date createdAt,
        Date updatedAt,
        boolean banned
) {

    public User(
            UUID userId,
            String username,
            String password,
            String name,
            String email,
            UserRole role,
            Date createdAt,
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
                new Date(),
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
                new Date(),
                banned
        );
    }
}
