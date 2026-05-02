package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {

    @NotNull
    private UUID storageId;

    @NotNull
    private List<UUID> cellIds;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Duration bookingTime;

}
