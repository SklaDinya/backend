package skladinya.application.dtos.v1.cell;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.Duration;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CellSearchParamsDto {

    @NotNull
    private OffsetDateTime startBooking;

    @NotNull
    private Duration timeBooking;

    private List<String> cellClasses;

    @PositiveOrZero
    private Integer pageNumber;

    @Positive
    private Integer pageSize;
}
