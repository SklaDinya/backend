package skladinya.utils.bdfiller.implementations.skladinya;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class UtilsFaker {
    private static Faker faker;
    private static Random random;

    @Autowired
    public void setFaker(Faker faker) {
        UtilsFaker.faker = faker;
    }

    @Autowired
    public void setRandom(Random random) {
        UtilsFaker.random = random;
    }

    static LocalDate generateLocalDateAfter(LocalDate afterDate, int maxDaysAfter) {
        Date startDate = Date.from(afterDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date futureDate = faker.date().future(maxDaysAfter, TimeUnit.DAYS, startDate);

        return futureDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    static LocalTime getTimeBetweenHours(int from, int to) {
        return LocalTime.of(
                faker.number().numberBetween(from, to),
                faker.number().numberBetween(10, 50)
        );
    }

    static String generateSentence(int minWordsCount, int maxWordsCount) {
        int wordCount = random.nextInt(maxWordsCount) + minWordsCount;

        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                sentence.append(" ");
            }
            sentence.append(faker.lorem().word());
        }

        return capitalizeFirst(sentence.toString());
    }

    static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
