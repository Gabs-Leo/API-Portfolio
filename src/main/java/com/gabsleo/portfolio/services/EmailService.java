package com.gabsleo.portfolio.services;

import com.gabsleo.portfolio.dtos.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${env.EMAIL_TARGET}")
    private String EMAIL_TARGET;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(EmailDto emailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailDto.email());
        message.setTo(EMAIL_TARGET);
        message.setSubject("Mensagem de "+ emailDto.name() +" sobre o portfólio!");
        message.setText(emailDto.message());
        mailSender.send(message);
    }
}
