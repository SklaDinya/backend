package skladinya.domain.models.booking;

import java.time.LocalDateTime;
import java.util.List;

public record BookingSearchOptions(
        LocalDateTime startBooking,
        LocalDateTime endBooking,
        List<BookingStatus> statuses,
        int pageSize,
        int pageNumber
) {
}
