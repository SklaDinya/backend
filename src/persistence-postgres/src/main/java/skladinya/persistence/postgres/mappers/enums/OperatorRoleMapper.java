package skladinya.persistence.postgres.mappers.enums;

import skladinya.domain.models.operator.OperatorRole;
import skladinya.persistence.postgres.entities.enums.OperatorRoleEntity;

public class OperatorRoleMapper {

    public static OperatorRoleEntity toEntity(OperatorRole domain) {
        return domain != null ? OperatorRoleEntity.valueOf(domain.name()) : null;
    }

    public static OperatorRole toDomain(OperatorRoleEntity entity) {
        return entity != null ? OperatorRole.valueOf(entity.name()) : null;
    }
}
