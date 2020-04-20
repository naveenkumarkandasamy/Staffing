package com.envision.Staffing.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@Service
public class EmailService {

	@Value("${useremail}")
	private String useremail;

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(String toEmail, String subject, String body, String attachment) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
			messageHelper.setSubject(subject);
			messageHelper.setText(body, true);
			messageHelper.setFrom(useremail);
			messageHelper.setTo(toEmail);
			InputStream input = null;
			messageHelper.addAttachment("attachment.txt", new ByteArrayDataSource(attachment, "text/plain"));
			mailSender.send(message);
		} catch (MessagingException | IOException ex) {
		}
	}

}
