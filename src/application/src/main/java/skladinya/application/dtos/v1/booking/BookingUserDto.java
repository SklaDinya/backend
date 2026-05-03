package skladinya.application.dtos.v1.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUserDto {

    @NotNull
    private UUID id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Email
    @Size(min = 1, max = 255)
    private String email;
}
