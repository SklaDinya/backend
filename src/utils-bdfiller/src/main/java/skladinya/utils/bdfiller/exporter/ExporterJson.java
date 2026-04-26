package skladinya.utils.bdfiller.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "json", havingValue = "true")
public class ExporterJson implements Exporter {
    public static String POSTFIX = ".json";
    private final ObjectMapper objectMapper;

    @Override
    public void export(String filename, List<Map<String, Object>> objects) {
        Path path = Path.of(filename + POSTFIX);
        try {
            clearFile(path);
        } catch (IOException e) {
            log.error("Cannot create or clear file: {}", filename);
            throw new RuntimeException("Cannot create or clear file: " + filename);
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path.toString()), objects);
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }
}
