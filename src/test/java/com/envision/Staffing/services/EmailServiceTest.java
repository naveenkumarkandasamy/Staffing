package com.envision.Staffing.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmailServiceTest {

	MimeMessage message;

	@InjectMocks
	EmailService emailService;

	@Mock
	JavaMailSender javaMailSender;

	@Before
	public void setUp() throws MessagingException {
		ReflectionTestUtils.setField(emailService, "username", "from@email.com");
		message = new JavaMailSenderImpl().createMimeMessage();
		doNothing().when(javaMailSender).send(any(MimeMessage.class));
	}

	@Test
	public void sendMailWithoutTemplate() throws Exception {

		String from = "from@email.com";
		String to = "to@gmail.com";
		String subject = "Test Email";
		String body = "<html>Test Email</html>";
		String attachment = "Test";
		when(javaMailSender.createMimeMessage()).thenReturn(message);
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
		messageHelper.setSubject(subject);
		messageHelper.setText(body, true);
		messageHelper.setFrom(from);
		messageHelper.setTo(to);
		messageHelper.addAttachment("attachment.txt", new ByteArrayDataSource(attachment, "text/plain"));

		emailService.sendMail(to, subject, body, attachment);
		Assert.assertNotNull(message);

	}
}
