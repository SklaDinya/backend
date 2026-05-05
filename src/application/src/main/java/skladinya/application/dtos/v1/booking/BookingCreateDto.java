package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.OffsetDateTime;
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
    @NotEmpty
    private List<UUID> cellIds;

    @NotNull
    private OffsetDateTime startTime;

    @NotNull
    private Duration bookingTime;

}
