package skladinya.utils.bdfiller.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public interface Exporter {
    void export(String filename, List<Map<String, Object>> objects);

    default void clearFile(Path path) throws IOException {
        Files.write(path, new byte[0], StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }
}
