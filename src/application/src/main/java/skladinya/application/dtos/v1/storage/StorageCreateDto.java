package skladinya.application.dtos.v1.storage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageCreateDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotBlank
    @Size(min = 1, max = 255)
    private String storageName;

    @NotBlank
    @Size(min = 1, max = 255)
    private String address;

    @Size(min = 1, max = 1000)
    private String description;
}
