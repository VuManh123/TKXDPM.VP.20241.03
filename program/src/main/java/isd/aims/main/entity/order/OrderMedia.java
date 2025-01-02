package isd.aims.main.entity.order;


import isd.aims.main.entity.media.Media;

public class OrderMedia {
    
    private Media media;
    private int price;
    private int quantity;

    public OrderMedia(Media media, int quantity, int price) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderMedia {" +
                "Media Title: " + media.getTitle() +
                ", Quantity: " + quantity +
                ", Price: " + price +
                "}";
    }


    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
