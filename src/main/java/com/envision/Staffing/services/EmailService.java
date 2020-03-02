package com.envision.Staffing.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	 @Autowired
	 private JavaMailSender mailSender;
	
	 @Autowired
	 private MailProperties mailProperties;
	 
	public void sendMail(String toEmail, String subject, String body, String attachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(mailProperties.getUsername());
            messageHelper.setTo(toEmail);
            InputStream input = null;
            messageHelper.addAttachment("attachment.txt", new ByteArrayDataSource( attachment,"text/plain"));
            System.out.println("Message sent successfully");

            mailSender.send(message);
        } catch (MessagingException | IOException ex) {
            System.out.println("Failed to send email to {}"+ toEmail);
        }
    }

}
