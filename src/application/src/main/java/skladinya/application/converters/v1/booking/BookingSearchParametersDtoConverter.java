package skladinya.application.converters.v1.booking;

import skladinya.application.dtos.v1.booking.BookingSearchParametersDto;
import skladinya.domain.models.booking.BookingSearchOptions;


public class BookingSearchParametersDtoConverter {

    private BookingSearchParametersDtoConverter() {
    }

    public static BookingSearchOptions toCoreEntity(BookingSearchParametersDto dto) {
        return new BookingSearchOptions(
                dto.getStartBooking(),
                dto.getEndBooking(),
                dto.getStatuses().stream().map(BookingStatusDtoConverter::toCoreEntity).toList(),
                dto.getPageSize(),
                dto.getPageNumber()
        );
    }

}
