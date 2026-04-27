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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "sql", havingValue = "true")
public class ExporterSql implements Exporter {
    public static String POSTFIX = ".sql";

    @Override
    public void export(String filename, List<Map<String, Object>> objects) {
        if (objects == null || objects.isEmpty()) {
            log.warn("No objects to export for file: {}", filename);
            return;
        }

        Path path = Path.of(filename + POSTFIX);

        try {
            clearFile(path);
        } catch (IOException e) {
            log.error("Cannot create or clear file: {}", filename);
            throw new RuntimeException("Cannot create or clear file: " + filename);
        }

        SqlInsertCommand builder = new SqlInsertCommand(
                path.getFileName().toString().split("\\.")[0],
                objects.getFirst().keySet()
        );

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            for (var object: objects) {
                writer.write(builder.values(object).build());
                writer.write("\n");
            }
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }

    private static class SqlInsertCommand {
        private final String table;
        private final String[] columnNames;
        private String[] values;

        public SqlInsertCommand(String tableName, Set<String> columnNames) {
            this.table = tableName;

            int index = 0;
            this.columnNames = new String[columnNames.size()];
            for (String colName : columnNames) {
                this.columnNames[index++] = colName;
            }
        }

        public SqlInsertCommand values(Map<String, Object> values) {
            this.values = new String[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                Object value = values.get(columnNames[i]);
                switch (value) {
                    case null -> this.values[i] = "NULL";
                    case String strVal -> this.values[i] = "'" + strVal.replace("'", "''") + "'";
                    case LocalDate dateVal -> this.values[i] = "'" + dateVal + "'";
                    case LocalDateTime timeVal -> this.values[i] = "'" + timeVal + "'";
                    default -> this.values[i] = value.toString();
                }
            }
            return this;
        }

        public String build() {
            return String.format(
                    "INSERT INTO %s (%s) VALUES (%s);",
                    table,
                    String.join(", ", columnNames),
                    String.join(", ", values)
            );
        }
    }
}
