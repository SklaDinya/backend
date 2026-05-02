package skladinya.application.converters.v1.booking;

import skladinya.application.dtos.v1.booking.BookingStatusDto;
import skladinya.domain.models.booking.BookingStatus;

public final class BookingStatusDtoConverter {

    private BookingStatusDtoConverter() {
    }

    public static BookingStatusDto toDto(BookingStatus status) {
        if (status == null) {
            return null;
        }
        return BookingStatusDto.valueOf(status.name());
    }

    public static BookingStatus toCoreEntity(BookingStatusDto dto) {
        if (dto == null) {
            return null;
        }
        return BookingStatus.valueOf(dto.name());
    }
}
