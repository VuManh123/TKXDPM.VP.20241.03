module isd.aims.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires com.google.api.services.gmail;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.extensions.jetty.auth;
    requires org.apache.commons.codec;
    requires mail;

    opens isd.aims.main to javafx.fxml;
    opens isd.aims.main.views to javafx.fxml;
    opens isd.aims.main.views.home to javafx.fxml;
    opens isd.aims.main.views.popup to javafx.fxml;
    opens isd.aims.main.views.cart to javafx.fxml;
    opens isd.aims.main.views.shipping to javafx.fxml;
    opens isd.aims.main.views.invoice to javafx.fxml;
    opens isd.aims.main.views.payment to javafx.fxml;
    opens isd.aims.main.views.order to javafx.fxml;
    opens isd.aims.main.views.mediadetail to javafx.fxml;

    exports isd.aims.main;
    exports isd.aims.main.views;
}
