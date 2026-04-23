package skladinya.persistence.postgres.mappers.enums;

import skladinya.domain.models.payment.PaymentType;
import skladinya.persistence.postgres.entities.enums.PaymentTypeEntity;

public class PaymentTypeMapper {

    public static PaymentTypeEntity toEntity(PaymentType domain) {
        return domain != null ? PaymentTypeEntity.valueOf(domain.name()) : null;
    }

    public static PaymentType toDomain(PaymentTypeEntity entity) {
        return entity != null ? PaymentType.valueOf(entity.name()) : null;
    }
}
