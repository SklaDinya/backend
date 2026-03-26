package skladinya.domain.models.booking;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record BookingCreate(
        UUID storageId,
        List<UUID> cellIds,
        Date startTime,
        Duration bookingTime
) {
}
