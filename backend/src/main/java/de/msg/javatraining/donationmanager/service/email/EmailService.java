package de.msg.javatraining.donationmanager.service.email;

import de.msg.javatraining.donationmanager.controller.email.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailService {
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("munteantudor03@gmail.com");
        message.setTo(emailRequest.getDestination());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getMessage());
        emailSender.send(message);
    }
}
