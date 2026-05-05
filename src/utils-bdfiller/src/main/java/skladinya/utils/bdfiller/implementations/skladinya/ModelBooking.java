package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;

import lombok.RequiredArgsConstructor;
import skladinya.utils.bdfiller.model.Model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum BookingStatus {
    Created,
    Paid,
    InProcess,
    Finished,
    Cancelled
}

@RequiredArgsConstructor
@Getter
public class ModelBooking extends Model {
    private final UUID bookingId;
    private final UUID userId;
    private final UUID storageId;
    private final List<UUID> cellIds;
    private final LocalDateTime startTime;
    private final Duration bookingTime;
    private final LocalDateTime endTime;
    private final LocalDateTime createdAt;
    private final BookingStatus status;
    private final BigDecimal price;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("booking_id", bookingId.toString());
        map.put("start_time", startTime);
        map.put("booking_time_hour", bookingTime.toHours());
        map.put("created_at", createdAt);
        map.put("finished_at", endTime);
        map.put("price", price);
        map.put("status", status.toString());
        map.put("storage_fk", storageId.toString());
        map.put("user_fk", userId.toString());
        return map;
    }
}
