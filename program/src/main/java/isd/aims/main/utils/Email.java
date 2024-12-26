package isd.aims.main.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Email {
    private final String host = "smtp.gmail.com"; // Máy chủ SMTP của Gmail
    private final String username;              // Địa chỉ email của bạn
    private final String password;              // Mật khẩu ứng dụng

    public Email(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        // Thiết lập các thuộc tính cho việc kết nối với SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo một session mới với thông tin xác thực
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Tạo và gửi email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}