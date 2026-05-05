package skladinya.application.dtos.v1.storage;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageSearchParamsDto {

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String address;

    @PositiveOrZero
    private Integer pageNumber;

    @Positive
    private Integer pageSize;
}
