package skladinya.persistence.postgres.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cells")
@Getter
@Setter
@NoArgsConstructor
public class CellEntity {

    @Id
    @Column(name = "cell_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cell_class", nullable = false)
    private String cellClass;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "storage_fk", nullable = false)
    private UUID storageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_fk", insertable = false, updatable = false)
    private StorageEntity storage;

    @ManyToMany(mappedBy = "cells", fetch = FetchType.LAZY)
    private Set<BookingEntity> bookings;

    public CellEntity(
            UUID id,
            String name,
            String cellClass,
            LocalDateTime createdAt,
            StorageEntity storage
    ) {
        this.id = id;
        this.name = name;
        this.cellClass = cellClass;
        this.createdAt = createdAt;
        this.storage = storage;
    }

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
        this.storageId = storage != null ? storage.getId() : null;
    }
}
