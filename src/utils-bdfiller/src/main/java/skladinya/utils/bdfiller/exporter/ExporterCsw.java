package skladinya.utils.bdfiller.exporter;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.config.OpenCsvConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "csw", havingValue = "true")
public class ExporterCsw implements Exporter {
    public static String POSTFIX = ".csw";
    private final OpenCsvConfig.CSVWriterBuilder csvWriterBuilder;

    @Override
    public void export(String filename, List<Map<String, Object>> objects) {
        if (objects.isEmpty()) return;

        Path path = Path.of(filename + POSTFIX);
        try {
            clearFile(path);
        } catch (IOException e) {
            log.error("Cannot create or clear file: {}", filename);
            throw new RuntimeException("Cannot create or clear file: " + filename);
        }

        try (CSVWriter writer = csvWriterBuilder.build(new FileWriter(path.toString()))) {
            writer.writeNext(objects.getFirst().keySet().toArray(new String[0]));

            for (Map<String, Object> row : objects) {
                writer.writeNext(row.values().stream().map(Object::toString).toArray(String[]::new));
            }
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }
}
