# PlaceOrder_Outline.md

## Use Case: Đặt Hàng (Place Order)

### **1. Basic Flow of the Event**

1. **Bắt Đầu (Start)**: Khách hàng truy cập vào phần mềm AIMS.
2. **Xem Sản Phẩm (View Products)**: Khách hàng duyệt danh sách 20 sản phẩm ngẫu nhiên hiển thị trên trang chính.
3. **Tìm Kiếm Sản Phẩm (Search Products)**: Khách hàng sử dụng chức năng tìm kiếm để tìm sản phẩm cụ thể.
4. **Xem Chi Tiết Sản Phẩm (View Product Details)**: Khách hàng chọn một sản phẩm để xem thông tin chi tiết.
5. **Thêm Vào Giỏ Hàng (Add to Cart)**: Khách hàng thêm các sản phẩm mong muốn với số lượng vào giỏ hàng.
6. **Xem Giỏ Hàng (View Cart)**: Khách hàng xem giỏ hàng, kiểm tra tổng giá và thông tin sản phẩm.
7. **Xem Lại Giỏ Hàng (Review Cart)**: Khách hàng xem lại các mặt hàng trong giỏ hàng, điều chỉnh số lượng nếu cần hoặc xóa sản phẩm.
8. **Cung Cấp Thông Tin Giao Hàng (Provide Delivery Information)**: Khách hàng nhập thông tin giao hàng bao gồm tên người nhận, email, số điện thoại và địa chỉ giao hàng.
9. **Cung Cấp Thông Tin Thanh Toán (Provide Payment Information)**: Khách hàng nhập thông tin thanh toán.
10. **Chọn Phương Thức Thanh Toán (Select Payment Method)**: Khách hàng chọn phương thức thanh toán (thẻ tín dụng).
11. **Xác Nhận Đơn Hàng (Confirm Order)**: Khách hàng xác nhận đơn hàng và tiến hành thanh toán.
12. **Xử Lý Thanh Toán (Process Payment)**: Hệ thống xử lý thanh toán qua VNPay.
13. **Tạo Hóa Đơn (Generate Invoice)**: Hệ thống tạo và hiển thị thông tin hóa đơn bao gồm chi tiết sản phẩm, tổng số tiền và phí giao hàng.
14. **Gửi Xác Nhận (Send Confirmation)**: Hệ thống gửi xác nhận đơn hàng và hóa đơn qua email cho khách hàng.
15. **Kết Thúc (End)**: Đơn hàng được đặt, và hệ thống cập nhật trạng thái đơn hàng.

### **2. Alternative Flows of the Event**

#### **Alternative Flow 1: Thiếu Hàng Trong Kho (Insufficient Inventory)**

1. Khách hàng thêm sản phẩm vào giỏ hàng.
2. Hệ thống kiểm tra mức tồn kho.
3. Nếu tồn kho không đủ, hệ thống thông báo cho khách hàng về tình trạng thiếu hàng.
4. Khách hàng cập nhật giỏ hàng (ví dụ: giảm số lượng hoặc xóa mặt hàng).
5. Khách hàng tiếp tục với giỏ hàng đã cập nhật.

#### **Alternative Flow 2: Thông Tin Thanh Toán Không Hợp Lệ (Invalid Payment Information)**

1. Khách hàng cung cấp thông tin thanh toán.
2. Hệ thống xác thực thông tin thanh toán.
3. Nếu thông tin thanh toán không hợp lệ, hệ thống yêu cầu khách hàng sửa chữa thông tin.
4. Khách hàng cập nhật thông tin thanh toán và gửi lại.

#### **Alternative Flow 3: Thiếu Thông Tin Giao Hàng (Missing Delivery Information)**

1. Khách hàng cung cấp thông tin giao hàng không đầy đủ.
2. Hệ thống yêu cầu khách hàng điền vào các trường thông tin còn thiếu.
3. Khách hàng hoàn tất thông tin giao hàng.
4. Hệ thống xử lý đơn hàng với thông tin đầy đủ.

#### **Alternative Flow 4: Hủy Đơn Hàng (Order Cancellation)**

1. Khách hàng quyết định hủy đơn hàng trước khi xác nhận.
2. Khách hàng truy cập vào tóm tắt đơn hàng hoặc giỏ hàng.
3. Khách hàng chọn tùy chọn hủy đơn hàng.
4. Hệ thống cập nhật trạng thái đơn hàng thành "Đã hủy."
5. Hệ thống gửi xác nhận hủy đơn hàng đến email của khách hàng.

#### **Alternative Flow 5: Giao Hàng Gấp (Rush Order Delivery)**

1. Khách hàng chọn giao hàng gấp.
2. Hệ thống kiểm tra tính đủ điều kiện cho giao hàng gấp.
3. Nếu đủ điều kiện, hệ thống yêu cầu thông tin giao hàng gấp bổ sung (ví dụ: thời gian giao hàng).
4. Khách hàng cung cấp thông tin giao hàng gấp.
5. Hệ thống cập nhật tùy chọn giao hàng và tính phí.
6. Khách hàng xác nhận đơn hàng gấp.