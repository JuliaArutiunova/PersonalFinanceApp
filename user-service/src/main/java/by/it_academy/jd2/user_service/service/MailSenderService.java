package by.it_academy.jd2.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String from;
    private String title = "PersonalFinances верификация";
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);
        message.setFrom(from);

        javaMailSender.send(message);
    }
}
