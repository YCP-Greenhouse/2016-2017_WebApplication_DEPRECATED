package controller;

import model.ContactModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

        System.out.println("Username: " + contact.getUsername());

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

            conn.commit();
            conn.close();

        } catch( SQLException e ) {

        }
    }

    public void updateContact(ContactModel contact) {
        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "UPDATE contactlist SET name='" + contact.getUsername() + "', position='" + contact.getPosition() + "', email='" + contact.getEmail() + "', phone='" + contact.getPhoneNumber() + "' WHERE id='" + contact.getId() + "'";

        System.out.println(sql);
        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();

            conn.commit();
            conn.close();


        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }

    }

    public ArrayList<ContactModel> getAllContacts() {
        ArrayList<ContactModel> contactList = new ArrayList<>();

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
                ContactModel contact = new ContactModel();

                contact.setId(rs.getInt(1));
                contact.setUsername(rs.getString(2));
                contact.setPosition(rs.getString(3));
                contact.setEmail(rs.getString(4));
                contact.setPhoneNumber(rs.getString(5));

                contactList.add(contact);
            }

            rs.close();
            ps.close();

            conn.commit();
            conn.close();

        } catch( SQLException e ) {

        }

        return contactList;
    }

    public JSONArray getAllContactsJSON() {
        JSONArray arr = new JSONArray();

        ArrayList<ContactModel> contactList = getAllContacts();

        try {
            for( ContactModel contact : contactList) {

                    JSONObject obj = new JSONObject();

                    obj.put("id", contact.getId());
                    obj.put("name", contact.getUsername());
                    obj.put("position", contact.getPosition());
                    obj.put("email", contact.getEmail());
                    obj.put("phone", contact.getPhoneNumber());

                    // Add contact object to JSONArray
                    arr.put(obj);
            }

        } catch( JSONException e ) {

        }

        return arr;
    }
}
