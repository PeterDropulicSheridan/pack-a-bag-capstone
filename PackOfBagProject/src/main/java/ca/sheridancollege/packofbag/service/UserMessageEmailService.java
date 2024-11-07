package ca.sheridancollege.packofbag.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserMessageEmailService {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageEmailService.class);
    private static final String RECEIVER_EMAIL = "packabagfoundations@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    public void sendContactEmail(String name, String email, String message) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(RECEIVER_EMAIL);
            helper.setSubject("Contact Form Submission from " + name);

            String emailContent = buildEmailContent(name, email, message);
            helper.setText(emailContent, true);

            emailSender.send(mimeMessage);
            logger.info("Contact form email successfully sent from {} to {}", email, RECEIVER_EMAIL);

        } catch (MessagingException e) {
            logger.error("Failed to send contact form email from {} to {}: {}", email, RECEIVER_EMAIL, e.getMessage());
        }
    }

    private String buildEmailContent(String name, String email, String message) {
        return String.format(
            """
            <html>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
                <div style="background-color: #f1f1f1; padding: 20px; text-align: center;">
                    <h2 style="color: #333;">Contact Form Submission</h2>
                    <p style="font-size: 16px; color: #333;">
                        You have received a new message from your website contact form.
                    </p>
                </div>
                <div style="padding: 20px;">
                    <p><strong>Name:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Message:</strong><br>%s</p>
                </div>
                <div style="background-color: #0056b3; color: white; padding: 10px; text-align: center;">
                    <p style="margin: 0;">Pack-a-Bag Foundations</p>
                    <p style="margin: 0; font-size: 14px;">Website Contact Form</p>
                </div>
            </body>
            </html>
            """,
            name, email, message);
    }
}
