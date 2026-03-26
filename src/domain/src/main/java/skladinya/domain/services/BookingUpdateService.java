package skladinya.domain.services;

import java.util.UUID;

public interface BookingUpdateService {

    void setDelayedCancel(UUID bookingId);

    void updateBookingsStatuses();
}
