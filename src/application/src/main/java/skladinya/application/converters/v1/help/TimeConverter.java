package skladinya.application.converters.v1.help;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class TimeConverter {

    private static final ZoneId MSK_ZONE = ZoneId.of("Europe/Moscow");

    private TimeConverter() {
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime time) {
        return time.atZone(MSK_ZONE).toOffsetDateTime();
    }

    public static LocalDateTime toLocalDateTime(OffsetDateTime time) {
        return time.atZoneSameInstant(MSK_ZONE).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
