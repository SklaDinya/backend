package skladinya.persistence.mappers;

import skladinya.domain.models.user.User;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.enums.UserRoleMapper;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        UserEntity entity = user != null ? new UserEntity() : null;

        if (entity != null) {
            entity.setId(user.userId());
            entity.setUsername(user.username());
            entity.setPassword(user.password());
            entity.setName(user.name());
            entity.setEmail(user.email());
            entity.setRole(UserRoleMapper.toEntity(user.role()));
            entity.setCreatedAt(user.createdAt());
            entity.setUpdatedAt(user.updatedAt());
            entity.setBanned(user.banned());
        }

        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return entity != null
                ? new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getName(),
                entity.getEmail(),
                UserRoleMapper.toDomain(entity.getRole()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isBanned()
        )
                : null;
    }
}
