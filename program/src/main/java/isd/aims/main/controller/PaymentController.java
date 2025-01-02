package isd.aims.main.controller;

import com.google.api.client.auth.oauth2.Credential;
import isd.aims.main.InterbankSubsystem.IPayment;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;
import isd.aims.main.dao.OrderDAO;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.media.Media;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.listener.TransactionResultListener;
import isd.aims.main.utils.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


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
				System.out.println(Cart.getCart().getListMedia());
				System.out.println(Order.getInstance().getId());

				// Lưu đơn hàng vào bảng Order, Transaction, Delivery và OrderMedia
				OrderDAO.insertOrder(Order.getInstance().getId(), Order.getInstance().getAmount(), Order.getInstance().getShippingFees());

				// Lấy thông tin từ giỏ hàng
				List<CartMedia> cartItems = Cart.getCart().getListMedia();

				for (CartMedia cartMedia : cartItems) {
					// Lấy thông tin media và quantity từ CartMedia
					Media media = cartMedia.getMedia();
					int mediaId = media.getId();
					int quantity = cartMedia.getQuantity();

					// Gọi phương thức insertOrderMedia
					OrderDAO.insertOrderMedia(Order.getInstance().getId(), mediaId, quantity);
				}

				transactionResult.save(Order.getInstance().getId());


				emptyCart();
				emptyOrder();

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
	public void emptyOrder(){
		Order.getInstance().emptyOrder();
	}
}
