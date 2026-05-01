package skladinya.application.dtos.v1.price;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceGetDto {

    @NotNull
    private UUID priceId;

    @NotNull
    private UUID storageId;

    @NotNull
    @Size(min = 1, max = 100)
    private String cellClass;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDateTime createdAt;

}
