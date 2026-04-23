package skladinya.persistence.postgres.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
public class PriceEntity {

    @Id
    @Column(name = "price_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "cell_class", nullable = false)
    private String cellClass;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "storage_fk", nullable = false)
    private UUID storageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_fk", insertable = false, updatable = false)
    private StorageEntity storage;

    public PriceEntity(UUID id, String cellClass, BigDecimal price, LocalDateTime createdAt, StorageEntity storage) {
        this.id = id;
        this.cellClass = cellClass;
        this.price = price;
        this.createdAt = createdAt;
        this.storage = storage;
    }

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
        this.storageId = storage != null ? storage.getId() : null;
    }
}
