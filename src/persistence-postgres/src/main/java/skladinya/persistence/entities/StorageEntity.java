package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skladinya.domain.models.storage.StorageStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "storages")
@Getter
@NoArgsConstructor
public class StorageEntity {

    @Id
    @Column(name = "storage_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StorageStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CellEntity> cells;

    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingEntity> bookings;

    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceEntity> prices;

    @OneToOne(mappedBy = "storage", fetch = FetchType.LAZY)
    private OperatorEntity operator;

    public StorageEntity(
            UUID id,
            String name,
            String address,
            String description,
            StorageStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
