package Management;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static Connection con;

    public static Connection getConnection(){

        /*
        File DBFile = new File("DB/BMS_DB.db");
        String DBPath = String.format("jdbc:sqlite:%s", DBFile.toURI().toString());
        */

        URL DB_URL = DBConnection.class.getResource("/DB/BMS_DB.db");
        String DBPath = String.format("jdbc:sqlite:%s", DB_URL.toString());

        try {
            con = DriverManager.getConnection(DBPath);
            System.out.println("Database Connection Successful!");
        } catch (SQLException e) {
            System.out.println("Database Connection Error!");
            e.printStackTrace();
        }
        return con;
    }

}
