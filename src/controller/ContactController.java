package controller;

import model.ContactModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactController {
    DatabaseController databaseController = new DatabaseController();

    public void addContact(ContactModel contact) {

        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "INSERT INTO contactlist VALUES(?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            ps.setString(1, null);
            ps.setString(2, contact.getUsername() );
            ps.setString(3, contact.getPosition() );
            ps.setString(4, contact.getEmail() );
            ps.setString(5, contact.getPhoneNumber() );

            ps.executeUpdate();
            ps.close();

        } catch( SQLException e ) {

        }
    }

    public JSONArray getAllContacts() {
        JSONArray arr = new JSONArray();

        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM contactlist";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();

                obj.put("id", rs.getInt(1));
                obj.put("name", rs.getString(2));
                obj.put("position", rs.getString(3));
                obj.put("email", rs.getString(4));
                obj.put("phone", rs.getString(5));

                // Add contact object to JSONArray
                arr.put(obj);
            }

        } catch( SQLException e ) {

        } catch( JSONException e ) {

        }

        return arr;
    }
}
