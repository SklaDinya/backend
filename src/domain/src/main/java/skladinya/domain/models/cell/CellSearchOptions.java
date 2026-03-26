package skladinya.domain.models.cell;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record CellSearchOptions(
        LocalDateTime startBooking,
        Duration timeBooking,
        List<String> cellClasses,
        int pageNumber,
        int pageSize) {
}
