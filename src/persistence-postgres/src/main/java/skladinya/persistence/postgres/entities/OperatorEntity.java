package skladinya.persistence.postgres.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skladinya.persistence.postgres.entities.enums.OperatorRoleEntity;

import java.util.UUID;

@Entity
@Table(name = "operators")
@Getter
@Setter
@NoArgsConstructor
public class OperatorEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private OperatorRoleEntity role;

    @Column(name = "user_fk", nullable = false)
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_fk", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "storage_fk", nullable = false)
    private UUID storageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_fk", insertable = false, updatable = false)
    private StorageEntity storage;

    public OperatorEntity(UUID id, OperatorRoleEntity role, UserEntity user, StorageEntity storage) {
        this.id = id;
        this.role = role;
        this.user = user;
        this.storage = storage;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public void setStorage(StorageEntity storage) {
        this.storage = storage;
        this.storageId = storage != null ? storage.getId() : null;
    }
}
