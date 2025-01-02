package isd.aims.main.utils;

import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.media.Media;

import javax.mail.*;
import javax.mail.internet.*;
import java.time.LocalDateTime;
import java.util.List;
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

    public void sendEmailOrder(String recipientEmail, int orderID, LocalDateTime transactionDate, double totalAmount, List<CartMedia> cartItems) throws MessagingException {
        // Tạo nội dung email
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Dear Customer,\n\n");

        orderDetails.append("Thank you for your order. Here are the details of your transaction:\n\n");
        orderDetails.append("Transaction Date: ").append(transactionDate).append("\n");
        orderDetails.append("Total Amount: ").append(String.format("%.2f", totalAmount)).append(" VND\n\n");
        orderDetails.append("Order Details:\n");

        cartItems.forEach(item -> {
            Media media = item.getMedia();
            orderDetails.append("- Product: ").append(media.getTitle()).append("\n");
            orderDetails.append("  Quantity: ").append(item.getQuantity()).append("\n");
            orderDetails.append("  Price: ").append(media.getPrice()).append(" VND\n\n");
        });

        orderDetails.append("Thank you for shopping with us!\n");
        orderDetails.append("Best regards,\n");
        orderDetails.append("AIMS Team");

        // Khởi tạo EmailSenderService với thông tin tài khoản email
        String senderEmail = "devvu203@gmail.com";
        String senderPassword = "zzgy xrxc clro fxpx"; // Mật khẩu ứng dụng (Google App Password)
        Email emailSender = new Email(senderEmail, senderPassword);

        // Gửi email
        String subject = "AIMS: Place Order Successfully!";
        String body = orderDetails.toString();

        try {
            emailSender.sendEmail(recipientEmail, subject, body);
            System.out.println("AIMS has sent to your email.");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

}