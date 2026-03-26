package skladinya.domain.models.cell;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public record CellSearchOptions(
        Date startBooking,
        Duration timeBooking,
        List<String> cellClasses,
        int pageNumber,
        int pageSize) {
}
