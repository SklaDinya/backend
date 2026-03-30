package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import skladinya.domain.models.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor
public class BookingEntity {

    @Id
    @Column(name = "booking_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "booking_time_hour", nullable = false)
    private Long bookingTimeHour;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_fk", nullable = false)
    private StorageEntity storage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_fk", nullable = false)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booking_cells",
            joinColumns = @JoinColumn(name = "booking_fk"),
            inverseJoinColumns = @JoinColumn(name = "cell_fk")
    )
    private Set<CellEntity> cells;

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentEntity payment;

    public BookingEntity(
            UUID id,
            LocalDateTime startTime,
            Long bookingTimeHour,
            LocalDateTime createdAt,
            BookingStatus status,
            StorageEntity storage,
            Set<CellEntity> cells,
            UserEntity user
    ) {
        this.id = id;
        this.startTime = startTime;
        this.bookingTimeHour = bookingTimeHour;
        this.createdAt = createdAt;
        this.status = status;
        this.storage = storage;
        this.cells = cells;
        this.user = user;
    }
}
