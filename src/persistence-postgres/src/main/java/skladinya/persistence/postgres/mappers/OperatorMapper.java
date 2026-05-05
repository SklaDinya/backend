package skladinya.persistence.postgres.mappers;

import skladinya.domain.models.operator.Operator;
import skladinya.persistence.postgres.entities.OperatorEntity;
import skladinya.persistence.postgres.mappers.enums.OperatorRoleMapper;

public class OperatorMapper {

    public static OperatorEntity toEntity(Operator operator) {
        OperatorEntity entity = operator != null ? new OperatorEntity() : null;

        if (entity != null) {
            entity.setId(operator.operatorId());
            entity.setRole(OperatorRoleMapper.toEntity(operator.role()));
            entity.setUserId(operator.userId());
            entity.setUser(UserMapper.toEntity(operator.user()));
            entity.setStorageId(operator.storageId());
        }

        return entity;
    }

    public static Operator toDomain(OperatorEntity entity) {
        return entity != null
                ? new Operator(
                entity.getId(),
                entity.getUserId(),
                UserMapper.toDomain(entity.getUser()),
                entity.getStorageId(),
                OperatorRoleMapper.toDomain(entity.getRole())
        )
                : null;
    }
}
