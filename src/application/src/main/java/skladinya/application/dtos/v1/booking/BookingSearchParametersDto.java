package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchParametersDto {

    @NotNull
    private LocalDateTime startBooking;

    @NotNull
    private LocalDateTime endBooking;

    @NotNull
    private List<BookingStatusDto> statuses;

    @NotNull
    private int pageSize;

    @NotNull
    private int pageNumber;

}
