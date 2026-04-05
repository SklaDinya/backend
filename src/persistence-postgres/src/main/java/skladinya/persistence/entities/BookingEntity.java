package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import skladinya.persistence.entities.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
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

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "storage_fk", nullable = false)
    private UUID storageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_fk", insertable = false, updatable = false)
    private StorageEntity storage;

    @Column(name = "user_fk", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_fk", insertable = false, updatable = false)
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

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
        this.storageId = storage != null ? storage.getId() : null;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }
}
