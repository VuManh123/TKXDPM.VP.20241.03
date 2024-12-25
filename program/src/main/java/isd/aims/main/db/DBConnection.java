package isd.aims.main.db;

import java.sql.DriverManager;
import java.util.logging.Logger;
import java.sql.Connection;
import isd.aims.main.utils.Utils;

public class DBConnection {

	private static Logger LOGGER = Utils.getLogger(Connection.class.getName());
	private static Connection connect;

    public static Connection getConnection() {
        if (connect != null) return connect;

        try {
//            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/aims","root","manhvu123");
//            // DB cua hung, dung xoa
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/aims","root","");
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
