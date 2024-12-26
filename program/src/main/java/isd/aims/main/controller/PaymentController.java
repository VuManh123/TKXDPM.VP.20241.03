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
				emptyCart(); // Làm trống giỏ hàng
				String subject = "Payment successful";
				String body = transactionResult.getMessage();
				String to = "vuducmanh10a@gmail.com"; // TODO: Add email to UI
				Email.sendMessage(to, subject, body);
				System.out.println("Lưu thành công");
			} catch (SQLException | IOException| GeneralSecurityException| MessagingException e) {
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
