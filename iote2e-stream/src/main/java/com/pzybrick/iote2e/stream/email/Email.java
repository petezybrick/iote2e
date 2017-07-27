package com.pzybrick.iote2e.stream.email;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Email {
	private static final Logger logger = LogManager.getLogger(Email.class);
	
	/*
	 * CRITICAL: to use SMTP w/ gmail, you have to allow less secure apps - https://myaccount.google.com/lesssecureapps
	 */
	
	public static void main(String[] args) throws IOException {
		try {
			sendEmail("your.personalized.medicine","iote2e2017$", "your.personalized.medicine@gmail.com", "drzybrick@gmail.com", "Dr. Zybrick", "Doe, John had a Blood Pressure of 120/80 at 2017-07-25 12:34:59" );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void sendEmail( String mailUser, String mailPassword, String from, String to, String salutation, String exceededSummary ) throws Exception {
		String content = String.format( "<html><body><b>Hello %s</b><br>Patient Metric Exceeded: %s<br>"
				+ "Launch the <a href=\"#\">Patient Dashboard</a><br>"
				+ "Regards,<br>Your Personalized Medicine"
				+ "</body></html>", salutation, exceededSummary);

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailUser, mailPassword);
				}
			});
		String subject = String.format("Patient Metric Exceeded: %s", exceededSummary);
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress( from ));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse( to ));
			message.setSubject(subject);
			message.setContent(content, "text/html");
			Transport.send(message);
			logger.info("email sent from: {}, to: {}, subject: {}", from, to, subject );
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
