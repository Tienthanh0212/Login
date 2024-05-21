package com.example.login.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;


    public void sendEmail(String to, String subject, String body){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper
                (message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);
            helper.setFrom("tienthanhpham44@gmail.com");//email cua ban than
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMailAdmin(String body){

        String subject="Xuất hiện người dùng đăng nhập lỗi";

        sendEmail("thanhpanda954@gmail.com",subject,body);
    }
}
