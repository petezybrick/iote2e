/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
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


/**
 * The Class Email.
 */
public class Email {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Email.class);
	
	/*
	 * CRITICAL: to use SMTP w/ gmail, you have to allow less secure apps - https://myaccount.google.com/lesssecureapps
	 */
	


	/**
	 * Send email bdbb.
	 *
	 * @param mailUser the mail user
	 * @param mailPassword the mail password
	 * @param from the from
	 * @param to the to
	 * @param salutation the salutation
	 * @param exceededSummary the exceeded summary
	 * @throws Exception the exception
	 */
	public static void sendEmailBdbb( String mailUser, String mailPassword, String from, String to, String salutation, String exceededSummary ) throws Exception {
		String content = String.format( "<html><body><b>Hello %s</b><br>Engine Metric Exceeded: %s<br>"
				+ "Launch the <a href=\"#\">Engine Dashboard</a><br>"
				+ "Regards,<br>Big Data Black Box"
				+ "</body></html>", salutation, exceededSummary);
		String subject = String.format("Engine Metric Exceeded: %s", exceededSummary);
		sendEmail( mailUser, mailPassword, from, to, salutation, subject, content );
	}
	
	/**
	 * Send email omh.
	 *
	 * @param mailUser the mail user
	 * @param mailPassword the mail password
	 * @param from the from
	 * @param to the to
	 * @param salutation the salutation
	 * @param exceededSummary the exceeded summary
	 * @throws Exception the exception
	 */
	public static void sendEmailOmh( String mailUser, String mailPassword, String from, String to, String salutation, String exceededSummary ) throws Exception {
		String content = String.format( "<html><body><b>Hello %s</b><br>Patient Metric Exceeded: %s<br>"
				+ "Launch the <a href=\"#\">Patient Dashboard</a><br>"
				+ "Regards,<br>Your Personalized Medicine"
				+ "</body></html>", salutation, exceededSummary);
		String subject = String.format("Patient Metric Exceeded: %s", exceededSummary);
		sendEmail( mailUser, mailPassword, from, to, salutation, subject, content );
	}

	/**
	 * Send email.
	 *
	 * @param mailUser the mail user
	 * @param mailPassword the mail password
	 * @param from the from
	 * @param to the to
	 * @param salutation the salutation
	 * @param subject the subject
	 * @param content the content
	 * @throws Exception the exception
	 */
	public static void sendEmail( String mailUser, String mailPassword, String from, String to, String salutation, String subject, String content  ) throws Exception {
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
