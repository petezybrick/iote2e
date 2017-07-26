package com.pzybrick.iote2e.stream.email;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.store.fs.FileUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

public class Email {
	private static final Logger logger = LogManager.getLogger(Email.class);
	private static final String APPLICATION_NAME = "iote2e-email";
	// CRITICAL: to be able to send gmails from any docker instance, i.e. from a Spark Streaming app running
	//    on a Spark worker, manually copy the file from <user.home>/.credentials/iote2e-email to
	//		the Docker shared folder that is shared among all containers - this is in every docker-compose.yml
	//		as /tmp/iote2e-shared, for example:
	//		cp /home/pete/.credentials/iote2e-email /home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared
	// NOTE: if you update/replace the credentials, make sure you copy to the docker shared folder
	private static final java.io.File DATA_STORE_DOCKER_DIR = new java.io.File("/tmp/iote2e-shared",
			".credentials/iote2e-email");
	private static final java.io.File DATA_STORE_LOCAL_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/iote2e-email");
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.MAIL_GOOGLE_COM);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			File fileDataStore = null;
			if( FileUtils.isDirectory(DATA_STORE_DOCKER_DIR.getParent()))
				fileDataStore = DATA_STORE_DOCKER_DIR;
			else fileDataStore = DATA_STORE_LOCAL_DIR;
			DATA_STORE_FACTORY = new FileDataStoreFactory(fileDataStore);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = Email.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * 
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public static Gmail getGmailService() throws IOException {
		Credential credential = authorize();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws IOException {
		// Build a new authorized API client service.
		try {
			sendEmail("your.personalized.medicine@gmail.com", "drzybrick@gmail.com", "Dr. ZybricK", "Doe, John had a Blood Pressure of 120/80 at 2017-07-25 12:34:59" );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void sendEmail(String from, String to, String salutation, String exceededSummary ) throws Exception {
		String content = String.format( "<html><body><b>Hello %s</b><br>Patient Metric Exceeded: %s<br>"
				+ "Launch the <a href=\"#\">Patient Dashboard</a><br>"
				+ "Regards,<br>Your Personalized Medicine"
				+ "</body></html>", salutation, exceededSummary);
		MimeMessage mimeMessage = createEmail(from, to, String.format("Patient Metric Exceeded: %s", exceededSummary), "");
		mimeMessage.setContent(content, "text/html");
		Gmail service = getGmailService();
		Message message = sendMessage(service, from, mimeMessage);
		logger.debug("Message: {}", message );
	}

	public static MimeMessage createEmail(String from, String to, String subject, String bodyText)
			throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public static Message sendMessage(Gmail service, String userId, MimeMessage emailContent)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(emailContent);
		message = service.users().messages().send(userId, message).execute();
		return message;
	}

}