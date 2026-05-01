package skladinya.services.email;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.services.EmailService;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SUBJECT_CREATED = "Пункт хранения создан";

    private static final String SUBJECT_APPROVED = "Заявка подтверждена";

    private static final String SUBJECT_REJECTED = "Заявка отклонена";

    private static final String TEMPLATE_CREATED = "storage-created.html";

    private static final String TEMPLATE_APPROVED = "storage-approved.html";

    private static final String TEMPLATE_REJECTED = "storage-rejected.html";

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendStorageCreated(String email, Storage storage) {
        sendMessage(email, SUBJECT_CREATED, TEMPLATE_CREATED, storage);
    }

    @Override
    public void sendStorageApproved(String email, Storage storage) {
        sendMessage(email, SUBJECT_APPROVED, TEMPLATE_APPROVED, storage);
    }

    @Override
    public void sendStorageRejected(String email, Storage storage) {
        sendMessage(email, SUBJECT_REJECTED, TEMPLATE_REJECTED, storage);
    }

    private void sendMessage(String to, String subject, String template, Storage storage) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            var context = new Context();
            context.setVariable("storage", storage);
            var content = templateEngine.process(template, context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw SklaDinyaException.wrap(e);
        }
    }
}
