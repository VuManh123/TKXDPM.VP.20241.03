package isd.aims.main.entity.order;

import isd.aims.main.utils.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order {

    private int shippingFees;
    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;
    private Integer id;

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public Order(int id, int shippingFees){
        this.id = id;
        this.shippingFees = shippingFees;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order Details:").append("\n");
        sb.append("ID: ").append(id == null ? "Not Assigned" : id).append("\n");
        sb.append("Shipping Fees: ").append(shippingFees).append("\n");
        sb.append("Delivery Info: ").append(deliveryInfo != null ? deliveryInfo : "Not Provided").append("\n");
        sb.append("Order Media List:").append("\n");

        for (Object om : lstOrderMedia) {
            sb.append(om.toString()).append("\n");
        }
        return sb.toString();
    }
}
