package skladinya.utils.bdfiller.implementations.skladinya;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    static String getCellClass() {
        return String.valueOf("ABC".charAt(random.nextInt(3)));
    }

    static String getCellClasses() {
        return "ABC";
    }

    static BigDecimal getClassPrice(String cellClass) {
        Map<String, BigDecimal> prices = Map.of("A", BigDecimal.ONE, "B", BigDecimal.TEN, "C", BigDecimal.valueOf(100));
        return prices.getOrDefault(cellClass, BigDecimal.valueOf(0));
    }

    static String generateSentence(int minWordsCount, int maxWordsCount) {
        String[] russianWords = {
                "Привет", "Мир", "Программист", "Код", "Работа", "Дом", "Машина",
                "Солнце", "Луна", "Звезда", "Друг", "Книга", "Стол", "Компьютер",
                "Интернет", "Утро", "Вечер", "День", "Ночь", "Красивый"
        };

        int wordCount = random.nextInt(minWordsCount, maxWordsCount + 1);

        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                sentence.append(" ");
            }
            String word = russianWords[random.nextInt(russianWords.length)];

            if (i == 0) {
                sentence.append(word);
            } else {
                sentence.append(word.toLowerCase());
            }
        }

        sentence.append(".");
        return sentence.toString();
    }

    static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
