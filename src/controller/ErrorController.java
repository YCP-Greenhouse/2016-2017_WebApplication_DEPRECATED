package controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import model.ContactModel;
import model.ErrorModel;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.MultiPartEmail;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ErrorController {

    DatabaseController dbController = new DatabaseController();
    ContactController contactController = new ContactController();
    ErrorModel errorModel = new ErrorModel();

    public void alertAdmins(ErrorModel error) {

        // Twilio configurations
        String ACCOUNT_SID = "AC73e88c0373966b37338518a7dd9dc190";
        String AUTH_TOKEN = "5796534f03e9046b576e906aa6aa8477";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);



        ArrayList<ContactModel> contactList = contactController.getAllContacts();

        sendEmail(error, contactList);

        for( ContactModel contact : contactList ) {
            Message message = Message.creator(new PhoneNumber(contact.getPhoneNumber()),
                    new PhoneNumber("+14842355187"),
                    error.getMessage()).create();



            System.out.println(message.getSid());
        }


    }

    public static void sendEmail(ErrorModel error, ArrayList<ContactModel> contactList ) {
        String myEmailId = "goodegreenhousealerts@gmail.com";
        String myPassword = System.getenv("EMAIL_PASSWORD");

        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(myEmailId, myPassword));
            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.setFrom(myEmailId);
            email.setSubject("Greenhouse Alert");
            email.setMsg("Error: " + error.getMessage() + "\nTime: " + error.getTime());

            for( ContactModel contact : contactList ) {
                email.addTo(contact.getEmail());
            }

            email.setTLS(true);


            email.send();
            System.out.println("Mail sent!");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
        }
    }


    public JSONObject getLastError() {
        JSONObject obj = new JSONObject();

        if( errorModel.equals(null) || errorModel.getCode() == 0 ) {
            getLastErrorFromDatabase();
        }

        try {
            obj.put("message", errorModel.getMessage());
            obj.put("time", errorModel.getTime());
            obj.put("code", errorModel.getCode());

        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return obj;
    }

    public void setLastError( ErrorModel model ) {
        errorModel = model;
    }

    public void updateError( ErrorModel error ) {

        // Check for invalid input
        if( !dbController.isValidInput(error.getMessage()) ){
            System.out.println("ErrorController: UpdateError(): Error message contains invalid characters");
            return;
        }
        setLastError(error);
        saveErrorToDatabase(error);
        alertAdmins(error);

    }

    public void saveErrorToDatabase( ErrorModel model ) {

        System.out.println("Save error to database");

        // Get website information from DB
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "INSERT INTO errors VALUES(?,?,?,?)";

        try {
            conn.setAutoCommit(false);


                ps = conn.prepareStatement(sql);

                ps.setString(1, null );
                ps.setString(2, model.getMessage() );
                ps.setString(3, model.getTime() );
                ps.setInt(4, model.getCode());

                ps.executeUpdate();
                ps.close();


            conn.commit();
            conn.close();


        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }

    }

    public void getLastErrorFromDatabase() {
        // Get website information from DB
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM errors";

        try {
            conn.setAutoCommit(false);


            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                errorModel.setCode(rs.getInt(4));
                errorModel.setMessage( rs.getString(2));
                errorModel.setTime(rs.getString(3));
            }

            rs.close();
            ps.close();

            conn.commit();
            conn.close();


        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }
    }




}
