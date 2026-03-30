package skladinya.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skladinya.domain.models.user.UserRole;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "banned", nullable = false)
    private boolean banned;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private OperatorEntity operator;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingEntity> bookings;

    public UserEntity(
            UUID id,
            String username,
            String password,
            String name,
            String email,
            UserRole role,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean banned
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.banned = banned;
    }
}
