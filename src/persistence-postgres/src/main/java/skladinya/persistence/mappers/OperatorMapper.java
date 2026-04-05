package skladinya.persistence.mappers;

import skladinya.domain.models.operator.Operator;
import skladinya.persistence.entities.OperatorEntity;
import skladinya.persistence.mappers.enums.OperatorRoleMapper;

public class OperatorMapper {

    public static OperatorEntity toEntity(Operator operator) {
        OperatorEntity entity = operator != null ? new OperatorEntity() : null;

        if (entity != null) {
            entity.setId(operator.operatorId());
            entity.setRole(OperatorRoleMapper.toEntity(operator.role()));
            entity.setUserId(operator.userId());
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
