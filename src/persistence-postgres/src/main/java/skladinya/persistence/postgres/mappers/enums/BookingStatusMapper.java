package skladinya.persistence.postgres.mappers.enums;

import skladinya.domain.models.booking.BookingStatus;
import skladinya.persistence.postgres.entities.enums.BookingStatusEntity;

public class BookingStatusMapper {

    public static BookingStatusEntity toEntity(BookingStatus domain) {
        return domain != null ? BookingStatusEntity.valueOf(domain.name()) : null;

    }

    public static BookingStatus toDomain(BookingStatusEntity entity) {
        return entity != null ? BookingStatus.valueOf(entity.name()) : null;
    }
}
