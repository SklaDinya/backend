package skladinya.application.dtos.v1.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMeDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    private UserRoleDto role;
}