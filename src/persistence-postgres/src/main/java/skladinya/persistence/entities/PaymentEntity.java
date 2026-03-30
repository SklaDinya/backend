package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skladinya.domain.models.payment.PaymentType;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_fk", nullable = false)
    private BookingEntity booking;

    public PaymentEntity(UUID id, PaymentType paymentType, String payload, BookingEntity booking) {
        this.id = id;
        this.paymentType = paymentType;
        this.payload = payload;
        this.booking = booking;
    }
}
