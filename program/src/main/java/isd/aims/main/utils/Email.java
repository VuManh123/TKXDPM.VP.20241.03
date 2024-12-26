package isd.aims.main.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.Credentials;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Email {
    private static final String TOKENS_DIRECTORY_PATH = "main/resources/isd/aims/main/tokens";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    private static Credential getCredentials( HttpTransport httpTransport, FileDataStoreFactory dataStoreFactory) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(Objects.requireNonNull(Email.class.getResourceAsStream(CREDENTIALS_FILE_PATH))));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static MimeMessage createEmail(String toEmailAddress, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("maxim080503@gmail.com"));
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setText(body);
        return email;
    }

    public static com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static void sendEmail(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        com.google.api.services.gmail.model.Message message = createMessageWithEmail(email);
        try {
            message = service.users().messages().send(userId, message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            System.err.println("Failed to send email: " + e.getDetails().getMessage());
            throw e;
        }
    }

    public static void sendMessage(String toEmailAddress, String subject, String body) throws IOException, GeneralSecurityException, MessagingException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH));
        Credential credential = getCredentials(httpTransport, dataStoreFactory);
        Gmail plus = new Gmail.Builder(
            httpTransport, JSON_FACTORY, credential
        ).setApplicationName("AIMS").build();
        MimeMessage email = createEmail(toEmailAddress, subject, body);
        sendEmail(plus, "me", email);
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException {
        sendMessage("maxim15976536428@gmail.com", "hi", "yes");
    }
}