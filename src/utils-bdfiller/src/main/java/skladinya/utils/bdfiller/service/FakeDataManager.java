package skladinya.utils.bdfiller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.exporter.Exporter;
import skladinya.utils.bdfiller.model.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class FakeDataManager {
    protected final List<? extends Exporter> exporters;
    protected final List<GeneratorModel<? extends Model>> generators;
    @Value("${config.export.outputDir: generatedData/}")
    protected String outputDir;

    public void run() {
        createDirIfNotExists();
        Map<String, List<? extends Model>> generated = generateAll();
        exportAll(generated);
    }

    protected Map<String, List<? extends Model>> generateAll() {
        Map<String, List<? extends Model>> context = new LinkedHashMap<>();

        for (GeneratorModel<? extends Model> g : generators) {
            List<? extends Model> out = g.generate(Collections.unmodifiableMap(context));
            if (context.containsKey(g.getModelName())) {
                throw new IllegalStateException("Duplicate generator name: " + g.getModelName());
            }
            context.put(g.getModelName(), out != null ? out : Collections.emptyList());
        }
        return context;
    }

    protected void exportAll(Map<String, List<? extends Model>> generated) {
        for (Map.Entry<String, List<? extends Model>> e : generated.entrySet()) {
            String modelName = e.getKey();

            List<Map<String, Object>> models = e.getValue().stream()
                    .map(Model::toMap)
                    .toList();

            for (Exporter ex : exporters) {
                ex.export(outputDir + modelName, models);
            }
        }
    }

    private void createDirIfNotExists() {
        Path path = Paths.get(outputDir);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Cannot create output directory: {}", outputDir);
                throw new RuntimeException("Cannot create output directory: " + outputDir);
            }
        }
    }
}
