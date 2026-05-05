package skladinya.services.booking.update;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import skladinya.domain.services.BookingUpdateService;

import java.util.concurrent.TimeUnit;

@Component
public class BookingScheduler {

    private final BookingUpdateService service;

    public BookingScheduler(BookingUpdateService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void run() {
        service.updateBookingsStatuses();
    }
}
