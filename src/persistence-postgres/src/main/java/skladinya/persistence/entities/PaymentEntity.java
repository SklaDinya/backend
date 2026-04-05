package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skladinya.persistence.entities.enums.PaymentType;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @Column(name = "payment_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "payload", nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "booking_fk", nullable = false)
    private UUID bookingId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_fk", insertable = false, updatable = false)
    private BookingEntity booking;

    public PaymentEntity(UUID id, PaymentType paymentType, String payload, BookingEntity booking) {
        this.id = id;
        this.paymentType = paymentType;
        this.payload = payload;
        this.booking = booking;
    }

    public void setBooking(BookingEntity booking) {
        this.booking = booking;
        this.bookingId = booking != null ? booking.getId() : null;
    }
}
