package skladinya.domain.models.booking;

import java.util.Date;
import java.util.List;

public record BookingSearchOptions(
        Date startBooking,
        Date endBooking,
        List<BookingStatus> statuses,
        int pageSize,
        int pageNumber
) {
}
