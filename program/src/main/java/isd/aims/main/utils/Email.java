package isd.aims.main.utils;

import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.media.Media;

import javax.mail.*;
import javax.mail.internet.*;
import java.security.SecureRandom;
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

    public String generateOtp(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("OTP length must be greater than zero.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Tạo số ngẫu nhiên từ 0 đến 9
        }

        return otp.toString();
    }

    public void sendOtp(String recipientEmail, String otp) {
        // Tạo nội dung email
        StringBuilder emailDetails = new StringBuilder();
        emailDetails.append("Dear Customer,\n\n");

        emailDetails.append("We have received a request to verify your account. Please use the following One-Time Password (OTP) to complete the verification process:\n\n");
        emailDetails.append("Your OTP: ").append(otp).append("\n\n");
        emailDetails.append("Note: This OTP is valid for 5 minutes. Please do not share this OTP with anyone for your account security.\n\n");

        emailDetails.append("If you did not request this OTP, please ignore this email or contact our support team for assistance.\n\n");

        emailDetails.append("Thank you for choosing AIMS.\n");
        emailDetails.append("Best regards,\n");
        emailDetails.append("The AIMS Team");

        // Khởi tạo EmailSenderService với thông tin tài khoản email
        String senderEmail = "devvu203@gmail.com";
        String senderPassword = "zzgy xrxc clro fxpx";
        Email emailSender = new Email(senderEmail, senderPassword);

        // Gửi email
        String subject = "AIMS: Verify Your OTP!";
        String body = emailDetails.toString();

        try {
            emailSender.sendEmail(recipientEmail, subject, body);
            System.out.println("OTP email has been sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }
    }

}