package skladinya.utils.bdfiller.config;

import com.opencsv.CSVWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCsvConfig {

    @Bean
    public CSVWriterBuilder csvWriterBuilder() {
        return new CSVWriterBuilder()
                .withSeparator(';')
                .withQuoteChar('"')
                .withEscapeChar('\\')
                .withLineEnd(System.lineSeparator());
    }

    public static class CSVWriterBuilder {
        private char separator = CSVWriter.DEFAULT_SEPARATOR;
        private char quoteChar = CSVWriter.DEFAULT_QUOTE_CHARACTER;
        private char escapeChar = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
        private String lineEnd = CSVWriter.DEFAULT_LINE_END;

        public CSVWriterBuilder withSeparator(char separator) {
            this.separator = separator;
            return this;
        }

        public CSVWriterBuilder withQuoteChar(char quoteChar) {
            this.quoteChar = quoteChar;
            return this;
        }

        public CSVWriterBuilder withEscapeChar(char escapeChar) {
            this.escapeChar = escapeChar;
            return this;
        }

        public CSVWriterBuilder withLineEnd(String lineEnd) {
            this.lineEnd = lineEnd;
            return this;
        }

        public CSVWriter build(java.io.Writer writer) {
            return new CSVWriter(writer, separator, quoteChar, escapeChar, lineEnd);
        }
    }
}
