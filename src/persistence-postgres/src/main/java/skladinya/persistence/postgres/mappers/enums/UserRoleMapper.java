package skladinya.persistence.postgres.mappers.enums;

import skladinya.domain.models.user.UserRole;
import skladinya.persistence.postgres.entities.enums.UserRoleEntity;

public class UserRoleMapper {

    public static UserRoleEntity toEntity(UserRole domain) {
        return domain != null ? UserRoleEntity.valueOf(domain.name()) : null;
    }

    public static UserRole toDomain(UserRoleEntity entity) {
        return entity != null ? UserRole.valueOf(entity.name()) : null;
    }
}
