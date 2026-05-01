package skladinya.services.booking.update;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.services.BookingUpdateService;

import java.util.UUID;

@Service
@RequestScope
public class BookingUpdateServiceImpl implements BookingUpdateService {
    @Override
    public void setDelayedCancel(UUID bookingId) {

    }

    @Override
    public void updateBookingsStatuses() {

    }
}
