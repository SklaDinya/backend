package skladinya.services.email;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.spring6.SpringTemplateEngine;
import skladinya.domain.services.EmailService;
import skladinya.persistence.redis.repositories.TestApplication;
import skladinya.tests.helper.builder.StorageBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=3025",
        "spring.mail.username=testuser",
        "spring.mail.password=testpass",
        "spring.mail.properties.mail.smtp.auth=true",
        "spring.mail.properties.mail.smtp.starttls.enable=false"
})
class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("testuser", "testpass"));

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailServiceImpl(emailSender, templateEngine);
    }

    @Test
    void sendStorageCreated_shouldSendMessage_whenCreated() throws MessagingException {
        var storage = StorageBuilder.builder().build();
        var email = "a@b.c";

        emailService.sendStorageCreated(email, storage);

        var received = greenMail.getReceivedMessages();
        assertEquals(1, received.length);
        assertTrue(received[0].getSubject().contains("Пункт хранения создан"));
        var content = GreenMailUtil.getBody(received[0]);
        assertTrue(content.contains(storage.name()));
        assertTrue(content.contains(storage.address()));
        assertTrue(content.contains(storage.description()));
    }

    @Test
    void sendStorageApproved_shouldSendMessage_whenCreated() throws MessagingException {
        var storage = StorageBuilder.builder().build();
        var email = "a@b.c";

        emailService.sendStorageApproved(email, storage);

        var received = greenMail.getReceivedMessages();
        assertEquals(1, received.length);
        assertTrue(received[0].getSubject().contains("Заявка подтверждена"));
        var content = GreenMailUtil.getBody(received[0]);
        assertTrue(content.contains(storage.name()));
        assertTrue(content.contains(storage.address()));
        assertTrue(content.contains(storage.description()));
    }

    @Test
    void sendStorageRejected_shouldSendMessage_whenCreated() throws MessagingException {
        var storage = StorageBuilder.builder().build();
        var email = "a@b.c";

        emailService.sendStorageRejected(email, storage);

        var received = greenMail.getReceivedMessages();
        assertEquals(1, received.length);
        assertTrue(received[0].getSubject().contains("Заявка отклонена"));
        var content = GreenMailUtil.getBody(received[0]);
        assertTrue(content.contains(storage.name()));
        assertTrue(content.contains(storage.address()));
        assertTrue(content.contains(storage.description()));
    }
}
