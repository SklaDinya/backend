package skladinya.persistence.mappers;

import skladinya.domain.models.user.User;
import skladinya.persistence.entities.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.userId(),
                user.username(),
                user.password(),
                user.name(),
                user.email(),
                user.role(),
                user.createdAt(),
                user.updatedAt(),
                user.banned()
        );
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isBanned()
        );
    }
}
