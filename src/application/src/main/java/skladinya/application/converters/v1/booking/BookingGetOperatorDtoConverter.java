package skladinya.application.converters.v1.booking;

import skladinya.application.converters.v1.cell.CellGetDtoConverter;
import skladinya.application.dtos.v1.booking.BookingGetOperatorDto;
import skladinya.domain.models.booking.Booking;

public final class BookingGetOperatorDtoConverter {

    private BookingGetOperatorDtoConverter() {
    }

    public static BookingGetOperatorDto toDto(Booking booking) {
        return BookingGetOperatorDto.builder()
                .id(booking.bookingId())
                .userId(booking.userId())
                .user(BookingUserDtoConverter.toDto(booking.user()))
                .storageId(booking.storageId())
                .cells(booking.cells().stream().map(CellGetDtoConverter::toDto).toList())
                .startTime(booking.startTime())
                .bookingTime(booking.bookingTime())
                .createdAt(booking.createdAt())
                .status(booking.status())
                .build();
    }

}
