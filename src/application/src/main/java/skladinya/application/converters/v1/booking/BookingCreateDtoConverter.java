package skladinya.application.converters.v1.booking;

import skladinya.application.dtos.v1.booking.BookingCreateDto;
import skladinya.domain.models.booking.BookingCreate;

public final class BookingCreateDtoConverter {

    private BookingCreateDtoConverter() {
    }

    public static BookingCreate toCoreEntity(BookingCreateDto dto) {
        return new BookingCreate(
                dto.getStorageId(),
                dto.getCellIds(),
                dto.getStartTime(),
                dto.getBookingTime()
        );
    }
}
