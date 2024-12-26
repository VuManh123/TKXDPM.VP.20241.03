package isd.aims.main.controller;

import com.google.api.client.auth.oauth2.Credential;
import isd.aims.main.InterbankSubsystem.IPayment;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.listener.TransactionResultListener;
import isd.aims.main.utils.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;


/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 */
public class PaymentController extends BaseController implements TransactionResultListener {

	private IPayment paymentService;
	private int amount;
	private String orderInfo;

	public PaymentController(IPayment vnPayService) {
		this.paymentService = vnPayService;
	}

	/**
	 * Generate VNPay payment URL
	 */

	public void payOrder(int amount, String orderInfo) throws IOException, SQLException {
		// Bắt đầu quy trình thanh toán
		new VnPaySubsystemController(this).payOrder(amount, orderInfo);
	}

	@Override
	public void onTransactionCompleted(PaymentTransaction transactionResult) {
		if (transactionResult != null && transactionResult.isSuccess()) {
			try {
				transactionResult.save(1); // Lưu giao dịch vào cơ sở dữ liệu nếu thành công
				// Lấy thông tin từ giỏ hàng
				Cart cart = Cart.getCart();
				StringBuilder orderDetails = new StringBuilder();
				orderDetails.append("Dear Customer,\n\n");
				orderDetails.append("Thank you for your order. Here are the details of your transaction:\n\n");
//				orderDetails.append("Transaction ID: ").append(transactionResult.getTransactionId()).append("\n");
//				orderDetails.append("Transaction Date: ").append(transactionResult.getCreatedAt()).append("\n");
//				orderDetails.append("Total Amount: ").append(transactionResult.getAmount()).append(" VND\n\n");
//				orderDetails.append("Order Details:\n");
//				cart.getItems().forEach(item -> {
//					orderDetails.append("- Product: ").append(item.getProduct().getName()).append("\n");
//					orderDetails.append("  Quantity: ").append(item.getQuantity()).append("\n");
//					orderDetails.append("  Price: ").append(item.getProduct().getPrice()).append(" VND\n\n");
//				});
				orderDetails.append("Thank you for shopping with us!\n");
				orderDetails.append("Best regards,\n");
				orderDetails.append("AIMS Team");
				emptyCart(); // Làm trống giỏ hàng

				// Khởi tạo EmailSenderService với thông tin tài khoản email
				String senderEmail = "devvu203@gmail.com";
				String senderPassword = "zzgy xrxc clro fxpx"; // Mật khẩu ứng dụng
				Email emailSender = new Email(senderEmail, senderPassword);

				// Gửi email
				String recipientEmail = "vuducmanh10a@gmail.com"; // Email người nhận
				String subject = "AIMS: Place Order Successfully!";
				String body = orderDetails.toString();

				try {
					emailSender.sendEmail(recipientEmail, subject, body);
					System.out.println("AIMS has sent to your email.");
				} catch (MessagingException e) {
					System.err.println("Failed to send email: " + e.getMessage());
					return;
				}

				System.out.println("Lưu thành công");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Giao dịch thất bại: " + (transactionResult != null ? transactionResult.getMessage() : "Lỗi không xác định"));
		}
	}

	public void emptyCart(){
        Cart.getCart().emptyCart();
    }
}
