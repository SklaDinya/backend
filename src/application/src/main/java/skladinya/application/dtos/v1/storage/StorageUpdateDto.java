package skladinya.application.dtos.v1.storage;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageUpdateDto {

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String address;

    @Size(min = 1, max = 1000)
    private String description;
}
