package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import skladinya.domain.models.booking.BookingStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGetOperatorDto {

    @NotNull
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private BookingUserDto user;

    @NotNull
    private UUID storageId;

//    @NotNull
//    private List<CellGetDto> cells;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Duration bookingTime;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private BookingStatus status;
}
