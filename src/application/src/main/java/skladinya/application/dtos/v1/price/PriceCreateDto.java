package skladinya.application.dtos.v1.price;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCreateDto {

    @NotNull
    @Size(min = 1, max = 100)
    private String cellClass;

    @NotNull
    private BigDecimal price;
}
