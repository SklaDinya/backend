package skladinya.application.dtos.v1.cell;

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
public class CellCreateDto {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String cellClass;
}