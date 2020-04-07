package com.envision.Staffing.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
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
	Logger log = Logger.getLogger(EmailService.class);

	public void sendMail(String toEmail, String subject, String body, String attachment) {
		log.info("Entering method for send mail :");
		log.info("Mail id :" + toEmail + " , subject for email :" + subject + " , bodyMessage :" + body);
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
			messageHelper.setSubject(subject);
			messageHelper.setText(body, true);
			messageHelper.setFrom(mailProperties.getUsername());
			messageHelper.setTo(toEmail);
			InputStream input = null;
			messageHelper.addAttachment("attachment.txt", new ByteArrayDataSource(attachment, "text/plain"));
			System.out.println("Message sent successfully");
			mailSender.send(message);
			log.info("Message sent Successfully to " + toEmail);
		} catch (MessagingException | IOException ex) {
			log.error("Failed to send email :", ex);
			System.out.println("Failed to send email to {}" + toEmail);
		}
	}

}
