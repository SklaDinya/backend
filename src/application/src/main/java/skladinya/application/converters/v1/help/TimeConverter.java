package skladinya.application.converters.v1.help;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TimeConverter {

    private TimeConverter() {
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC);
    }
}
