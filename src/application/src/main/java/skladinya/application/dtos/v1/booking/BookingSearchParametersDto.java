package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchParametersDto {

    @NotNull
    private OffsetDateTime startBooking;

    @NotNull
    private OffsetDateTime endBooking;

    private List<BookingStatusDto> statuses;

    @PositiveOrZero
    private Integer pageNumber;

    @Positive
    private Integer pageSize;

}
