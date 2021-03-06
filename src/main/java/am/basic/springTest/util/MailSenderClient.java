package am.basic.springTest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
public class MailSenderClient {


    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(from);
        emailSender.send(message);

    }
}
