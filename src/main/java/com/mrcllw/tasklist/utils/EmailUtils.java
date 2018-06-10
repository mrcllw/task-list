package com.mrcllw.tasklist.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String subject, String message, String emailTo){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setTo(emailTo);
        mailMessage.setFrom(emailFrom);
        javaMailSender.send(mailMessage);
        return true;
    }
}
