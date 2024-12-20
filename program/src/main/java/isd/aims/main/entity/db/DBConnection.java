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
//            String url = "jdbc:mysql://localhost:3306/aims";
//            String username = "root";
//            String password = "";

            // DB cua hung
            String url = "jdbc:mysql://localhost:3306/aims";
            String username = "root";
            String password = "";

            connect = DriverManager.getConnection(url,username,password);
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from cd");
            while (resultSet.next()){
                System.out.println("Yes");
            }
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
