package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountController {
    DatabaseController dbController = new DatabaseController();

    public boolean verifyUser( String userName, String password ) {

        System.out.println("Password: " + password );

        String hashedPassword = generateHash(password);

        System.out.println("Hashed password" + hashedPassword );

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return false;
        }


        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM accounts WHERE username='" + userName + "'";
        String dbPassword = "";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                dbPassword = rs.getString(3);
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return false;
        }

        // If password matches, return true
        if( dbPassword.equals(hashedPassword) ) {
            return true;
        }

        return false;
    }

    public boolean verifyAPIKey( String APIKey ) {

        String hashedKey = generateHash(APIKey);

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return false;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM apikeys WHERE apikey='" + hashedKey + "'";
        //String sql = "SELECT * FROM apikeys WHERE id='1'";
        boolean verified = false;

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                verified = true;
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return false;
        }

        return verified;
    }

    public String generateHash(String input) {
        String salt = System.getenv("RDS_SALT");

        String saltedPassword = salt + input;

        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(saltedPassword.getBytes());
            char[] digits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

            for( int i=0; i<hashedBytes.length; i++ ) {
                byte b = hashedBytes[i];
                hash.append(digits[b & 0xf0 >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }

        return hash.toString();
    }

}
