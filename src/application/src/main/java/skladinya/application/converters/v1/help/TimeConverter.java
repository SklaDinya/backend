package skladinya.application.converters.v1.help;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class TimeConverter {

    private TimeConverter() {
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static LocalDateTime toLocalDateTime(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
