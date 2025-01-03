package isd.aims.main.entity.order;

public class OrderProduct {
    private int orderID;
    private String title;
    private int price;
    private String imageUrl;
    private int numberOfProducts;

    public OrderProduct(int orderID, String title, int price, String imageUrl, int numberOfProducts) {
        this.orderID = orderID;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.numberOfProducts = numberOfProducts;
    }

    // Getters và setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getNumberOfProducts() { return numberOfProducts; }
    public void setNumberOfProducts(int numberOfProducts) { this.numberOfProducts = numberOfProducts; }

    @Override
    public String toString() {
        return "{" +
                "orderID=" + orderID +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", numberOfProducts=" + numberOfProducts +
                '}';
    }
}

