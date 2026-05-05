package skladinya.application.converters.v1.booking;

import skladinya.application.converters.v1.help.TimeConverter;
import skladinya.application.dtos.v1.booking.BookingSearchParametersDto;
import skladinya.domain.models.booking.BookingSearchOptions;

import java.util.List;


public class BookingSearchParametersDtoConverter {

    private BookingSearchParametersDtoConverter() {
    }

    public static BookingSearchOptions toCoreEntity(BookingSearchParametersDto dto) {
        return new BookingSearchOptions(
                TimeConverter.toLocalDateTime(dto.getStartBooking()),
                TimeConverter.toLocalDateTime(dto.getEndBooking()),
                dto.getStatuses() == null
                        ? List.of()
                        : dto.getStatuses().stream().map(BookingStatusDtoConverter::toCoreEntity).toList(),
                dto.getPageSize(),
                dto.getPageNumber()
        );
    }

}
