package skladinya.domain.services;

import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.payment.Payment;

public interface PaymentService {

    String createReceipt(Booking booking);

    Booking validateReceipt(String receipt);

    Payment payNoOp(Booking booking);

    Payment payRandom(Booking booking);

    void cancel(Payment payment);
}
