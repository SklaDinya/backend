package skladinya.persistence.mappers;

import skladinya.domain.models.operator.Operator;
import skladinya.persistence.entities.OperatorEntity;
import skladinya.persistence.entities.StorageEntity;
import skladinya.persistence.entities.UserEntity;

import java.util.UUID;

public class OperatorMapper {

    public static OperatorEntity toEntity(
            Operator operator,
            UserEntity user,
            StorageEntity storage
    ) {
        if (operator == null) {
            return null;
        }

        return new OperatorEntity(
                operator.operatorId(),
                operator.role(),
                user,
                storage
        );
    }

    public static Operator toDomain(OperatorEntity entity) {
        if (entity == null) {
            return null;
        }

        UUID userId = entity.getUser() != null
                ? entity.getUser().getId()
                : null;

        UUID storageId = entity.getStorage() != null
                ? entity.getStorage().getId()
                : null;

        return new Operator(
                entity.getId(),
                userId,
                null,
                storageId,
                entity.getRole()
        );
    }
}
