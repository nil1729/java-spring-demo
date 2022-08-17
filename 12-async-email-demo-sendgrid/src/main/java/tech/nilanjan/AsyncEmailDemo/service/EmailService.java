package tech.nilanjan.AsyncEmailDemo.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.nilanjan.AsyncEmailDemo.shared.dto.UserDto;
import tech.nilanjan.AsyncEmailDemo.shared.utils.SendGridKeys;

import java.io.IOException;

@Service
public class EmailService {
    private Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final String VERIFICATION_EMAIL_SUBJECT = "Verify your email address!";

    private Mail createMail(
            String destinationEmailAddress,
            String mailSubject,
            String mailBody
    ) {
        Email senderEmail = new Email(SendGridKeys.SENDER_EMAIL_ADDRESS);
        Email destinationEmail = new Email(destinationEmailAddress);
        Content content = new Content(ContentType.TEXT_PLAIN.getMimeType(), mailBody);
        return new Mail(senderEmail, mailSubject, destinationEmail, content);
    }

    private void sendEmail(Mail mailRequest) {
        try {
            SendGrid sg = new SendGrid(SendGridKeys.SEND_GRID_API_KEY);
            Request request = new Request();

            request.setEndpoint("mail/send");
            request.setBody(mailRequest.build());
            request.setMethod(Method.POST);

            sg.api(request);

            logger.info("Email sent successfully");
        } catch (IOException ex) {
            logger.error("Error Sending Email: " + ex.getMessage());
        }
    }

    @Async
    public void sendVerificationEmail(UserDto userDetails) {
        Mail mailObject = this.createMail(
                userDetails.getEmail(),
                this.VERIFICATION_EMAIL_SUBJECT,
                userDetails.getEmailVerificationToken()
        );

        /**
         *
         * Test: this method not block our main controller task
         *
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }

        this.sendEmail(mailObject);
    }
}
