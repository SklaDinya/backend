package skladinya.services.payment;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.services.PaymentService;

@Service
@RequestScope
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String createReceipt(Booking booking) {
        return "";
    }

    @Override
    public Booking validateReceipt(String receipt) {
        return null;
    }

    @Override
    public Payment payNoOp(Booking booking) {
        return null;
    }

    @Override
    public Payment payRandom(Booking booking) {
        return null;
    }

    @Override
    public void cancel(Payment payment) {

    }
}
