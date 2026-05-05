package skladinya.utils.bdfiller.exporter;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "txt", havingValue = "true")
public class ExporterText implements Exporter {
    public static String POSTFIX = ".txt";

    @Override
    public void export(String filename, List<Map<String, Object>> objects) {
        Path path = Path.of(filename + POSTFIX);

        try {
            clearFile(path);
        } catch (IOException e) {
            log.error("Cannot create or clear file: {}", filename);
            throw new RuntimeException("Cannot create or clear file: " + filename);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            for (var object : objects) {
                for (var entry : object.entrySet()) {
                    writer.write(entry.getKey() + ": " + entry.getValue());
                    writer.write("; ");
                }
                writer.write("\n");
            }
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }
}
