package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skladinya.domain.models.operator.OperatorRole;

import java.util.UUID;

@Entity
@Table(name = "operators")
@Getter
@NoArgsConstructor
public class OperatorEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Setter
    private OperatorRole role;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_fk", nullable = false)
    @Setter
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "storage_fk", nullable = false)
    @Setter
    private StorageEntity storage;

    public OperatorEntity(UUID id, OperatorRole role, UserEntity user, StorageEntity storage) {
        this.id = id;
        this.role = role;
        this.user = user;
        this.storage = storage;
    }
}
