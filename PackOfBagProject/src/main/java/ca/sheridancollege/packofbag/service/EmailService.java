package ca.sheridancollege.packofbag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendRegistrationEmail(String to, String eventTitle, String eventTime, String eventDate, String eventLocation) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Confirmation: You Are Registered for " + eventTitle);

            String emailContent = String.format(
                "<html>"
                + "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>"
                + "<div style='background-color: #f8f9fa; padding: 20px; text-align: center;'>"
                + "  <h2 style='color: #0056b3;'>Event Registration Confirmation</h2>"
                + "  <p style='font-size: 16px; color: #333;'>You have successfully registered for <strong>%s</strong>.</p>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "  <h3 style='color: #007bff;'>Event Details</h3>"
                + "  <table style='width: 100%%; border-collapse: collapse;'>"
                + "    <tr style='background-color: #f1f1f1;'><td style='padding: 10px; font-weight: bold;'>Event:</td><td style='padding: 10px;'>%s</td></tr>"
                + "    <tr><td style='padding: 10px; font-weight: bold;'>Date:</td><td style='padding: 10px;'>%s</td></tr>"
                + "    <tr style='background-color: #f1f1f1;'><td style='padding: 10px; font-weight: bold;'>Time:</td><td style='padding: 10px;'>%s</td></tr>"
                + "    <tr><td style='padding: 10px; font-weight: bold;'>Location:</td><td style='padding: 10px;'>%s</td></tr>"
                + "  </table>"
                + "  <p style='margin-top: 20px;'>Please arrive at least 15 minutes early to ensure a smooth check-in process. Should you have any questions, feel free to contact us at any time.</p>"
                + "  <p>We look forward to welcoming you!</p>"
                + "</div>"
                + "<div style='background-color: #0056b3; color: white; padding: 20px; text-align: center;'>"
                + "  <p style='margin: 0;'>Contact Us:</p>"
                + "  <p style='margin: 5px 0;'>Email: <a href='mailto:eventsupport@yourorganization.com' style='color: white;'>eventsupport@yourorganization.com</a></p>"
                + "  <p style='margin: 5px 0;'>Phone: (123) 456-7890</p>"
                + "</div>"
                + "</body>"
                + "</html>",
                eventTitle, eventTitle, eventDate, eventTime, eventLocation);

            helper.setText(emailContent, true); 
            emailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
