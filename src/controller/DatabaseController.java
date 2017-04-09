/* This controller establishes database connections and checks for invalid input */

package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("EST"));

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setCalendar(cal);

        //System.out.println( "Before daylight: " + dateFormat.format(cal.getTime()));

        // Check if daylight savings is active
        if( !TimeZone.getTimeZone("EST").inDaylightTime(cal.getTime()) ) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }

        dateFormat.setCalendar(cal);

        //System.out.println( "After daylight: " + dateFormat.format(cal.getTime()));

        return dateFormat.format(cal.getTime());

        /*
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date utcTime = new Date();

        System.out.println("Date: " + utcTime.toString() );

        Date date = new Date(utcTime.getTime() + TimeZone.getTimeZone("EST").getRawOffset() );

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //cal.add(Calendar.HOUR_OF_DAY, -4);

        // Check if daylight savings is active
        if( TimeZone.getTimeZone("EST").inDaylightTime(date) ) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }

        System.out.println("Updated Time: " + dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());*/
    }

    // Checks input for invalid String characters
    public boolean isValidInput(String input) {
        int count = 0;

        if( input == null ) {
            return false;
        }

        count = input.replaceAll("[^\";'`()~]", "").length();

        if( count == 0 ) {
            return true;
        } else {
            return false;
        }
    }
}
