package isd.aims.main.entity.invoice;

import java.time.LocalDate;

public class InvoiceDetails {
    private int orderID;
    private LocalDate date;
    private String address;
    private String email;
    private String phoneNumber;
    private double shippingFee;
    private double total;

    public InvoiceDetails(int orderID, LocalDate date, String address, String email, String phoneNumber, double shippingFee, double total) {
        this.orderID = orderID;
        this.date = date;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.shippingFee = shippingFee;
        this.total = total;
    }

    // Getters và setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return "InvoiceDetails{" +
                "orderID=" + orderID +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", shippingFee=" + shippingFee +
                ", total=" + total +
                '}';
    }
}
