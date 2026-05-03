package skladinya.application.converters.v1.booking;

import skladinya.application.converters.v1.cell.CellGetDtoConverter;
import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.converters.v1.storage.StorageGetDtoConverter;
import skladinya.application.dtos.v1.booking.BookingGetUserDto;
import skladinya.domain.models.booking.Booking;

public final class BookingGetUserDtoConverter {

    private BookingGetUserDtoConverter() {
    }

    public static BookingGetUserDto toDto(Booking booking) {
        return BookingGetUserDto.builder()
                .id(booking.bookingId())
                .userId(booking.userId())
                .storageId(booking.storageId())
                .storage(StorageGetDtoConverter.toDto(booking.storage()))
                .cells(booking.cells().stream().map(CellGetDtoConverter::toDto).toList())
                .startTime(TimeConverter.toOffsetDateTime(booking.startTime()))
                .bookingTime(booking.bookingTime())
                .price(booking.price())
                .createdAt(TimeConverter.toOffsetDateTime(booking.createdAt()))
                .status(booking.status())
                .build();
    }

}
