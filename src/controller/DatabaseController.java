/* This controller establishes database connections and checks for invalid input */

package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class DatabaseController {
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Connection when hosted in AWS
        if (System.getenv("RDS_HOSTNAME") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String dbName = System.getenv("RDS_DB_NAME");
                String userName = System.getenv("RDS_USERNAME");
                String password = System.getenv("RDS_PASSWORD");
                String hostname = System.getenv("RDS_HOSTNAME");
                String port = System.getenv("RDS_PORT");
                String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

                Connection con = DriverManager.getConnection(jdbcUrl);
                return con;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        // Dev Connection
        } else {
            connectionProps.put("user", "root");
            connectionProps.put("password", "root");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/greenhouse?autoReconnect=true&useSSL=true", connectionProps);
        }

        return conn;
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date utcTime = new Date();
        Date date = new Date(utcTime.getTime() + TimeZone.getTimeZone("EST").getRawOffset() );
        return dateFormat.format(date);
    }

    // Checks input for invalid String characters
    public boolean isValidInput(String input) {
        int count = 0;
        count = input.replaceAll("[^\";'`()~]", "").length();

        if( count == 0 ) {
            return true;
        } else {
            return false;
        }
    }
}