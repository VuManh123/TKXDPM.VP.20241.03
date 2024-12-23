package isd.aims.main.entity.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;
import java.sql.Connection;
import isd.aims.main.utils.Utils;

public class DBConnection {

	private static Logger LOGGER = Utils.getLogger(Connection.class.getName());
	private static Connection connect;

    public static Connection getConnection() {
        if (connect != null) return connect;

        try {
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/aims","root","motconvit123");
            return connect;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return connect;
        }
    }


    public static void main(String[] args) {
        DBConnection.getConnection();
    }
}
