package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private UUID storageId;

    @NotBlank
    private List<UUID> cellIds;

    @NotBlank
    private LocalDateTime startTime;

    @NotBlank
    private Duration bookingTime;

}
