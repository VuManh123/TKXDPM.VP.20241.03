package isd.aims.main.entity.cart;


import isd.aims.main.entity.media.Media;

public class CartMedia {
    
    private Media media;
    private int quantity;
    private int price;
    private int supportRushOrder;

    public CartMedia(Media media, Cart cart, int quantity, int price, int supportRushOrder) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
        this.supportRushOrder = supportRushOrder;
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
    public int getSupportRushOrder() {
        return this.supportRushOrder;
    }
    public void setSupportRushOrder(int supportRushOrder) {
        this.supportRushOrder = supportRushOrder;
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

    @Override
    public String toString() {
        return "{" 
            + " media='" + media + "'" 
            + ", quantity='" + quantity + "'" + ", supportRushOrder='" + supportRushOrder + "'"
            + "}";
    }

}

    
