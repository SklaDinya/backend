package skladinya.application.converters.v1.booking;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.booking.BookingCreateDto;
import skladinya.domain.models.booking.BookingCreate;

public final class BookingCreateDtoConverter {

    private BookingCreateDtoConverter() {
    }

    public static BookingCreate toCoreEntity(BookingCreateDto dto) {
        return new BookingCreate(
                dto.getStorageId(),
                dto.getCellIds(),
                TimeConverter.toLocalDateTime(dto.getStartTime()),
                dto.getBookingTime()
        );
    }
}
