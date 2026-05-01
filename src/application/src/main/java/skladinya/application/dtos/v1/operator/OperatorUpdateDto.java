package skladinya.application.dtos.v1.operator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorUpdateDto {

    @Size(min = 3, max = 50)
    private String username;

    @Size(min = 6, max = 100)
    private String password;

    @Size(min = 1, max = 255)
    private String name;

    @Email
    @Size(min = 1, max = 255)
    private String email;

    private OperatorRoleDto role;

    private Boolean banned;
}
