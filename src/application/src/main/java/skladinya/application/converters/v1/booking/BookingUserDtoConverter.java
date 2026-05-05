package skladinya.application.converters.v1.booking;

import skladinya.application.dtos.v1.booking.BookingUserDto;
import skladinya.domain.models.user.User;

public final class BookingUserDtoConverter {

    private BookingUserDtoConverter() {
    }

    public static BookingUserDto toDto(User user) {
        return BookingUserDto.builder()
                .id(user.userId())
                .name(user.name())
                .email(user.email())
                .build();
    }

}
