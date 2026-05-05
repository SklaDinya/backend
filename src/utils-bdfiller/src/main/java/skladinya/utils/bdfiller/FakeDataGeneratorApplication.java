package skladinya.utils.bdfiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import skladinya.utils.bdfiller.service.FakeDataManager;

@SpringBootApplication
public class FakeDataGeneratorApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(FakeDataGeneratorApplication.class, args);

        FakeDataManager generator = context.getBean(FakeDataManager.class);
        generator.run();
	}

}
