package skladinya.domain.models.booking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingCreate(
        UUID storageId,
        List<UUID> cellIds,
        LocalDateTime startTime,
        Duration bookingTime
) {
}
