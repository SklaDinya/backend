package skladinya.utils.bdfiller.implementations.skladinya;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import skladinya.utils.bdfiller.model.Model;
import skladinya.utils.bdfiller.service.GeneratorModel;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Getter
@Order(4)
public class GeneratorModelCell extends GeneratorModel<ModelCell> {
    private final PasswordEncoder encoder;

    public GeneratorModelCell(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    public static String getRandomName() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            char letter = (char) ('A' + ThreadLocalRandom.current().nextInt(26));
            sb.append(letter);
        }

        for (int i = 0; i < 5; i++) {
            int digit = ThreadLocalRandom.current().nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    private String getRandomClass() {
        String[] sizes = {"Малая", "Обычная", "Большая"};
        return sizes[ThreadLocalRandom.current().nextInt(sizes.length)];
    }

    @Override
    public String getModelName() {
        return "cells";
    }

    @Override
    public List<ModelCell> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> storages = context.getOrDefault("storages", Collections.emptyList());
        List<ModelCell> out = new ArrayList<>();

        for (Model model : storages) {
            if (!(model instanceof ModelStorage storage)) {
                continue;
            }
            int cntCells = faker.number().numberBetween(10, 20);
            for (int i = 0; i < cntCells; i++) {
                LocalDateTime createdDate = storage.getCreatedAt().plusDays(faker.number().numberBetween(1, 3));
                out.add(
                        new ModelCell(
                                UUID.randomUUID(),
                                storage.getStorageId(),
                                getRandomName(),
                                getRandomClass(),
                                createdDate
                        )
                );
            }
        }
        return out;
    }
}
