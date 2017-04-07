package controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import model.ErrorModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorController {

    DatabaseController dbController = new DatabaseController();
    ErrorModel errorModel = new ErrorModel();

    public void alertAdmins(String error) {
        String ACCOUNT_SID = "AC73e88c0373966b37338518a7dd9dc190";
        String AUTH_TOKEN = "5796534f03e9046b576e906aa6aa8477";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+16107870426"),
                new PhoneNumber("+14842355187"),
                error).create();

        System.out.println(message.getSid());

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

    public void updateError( ErrorModel model ) {

        // Check for invalid input
        if( !dbController.isValidInput(model.getMessage()) ){
            System.out.println("ErrorController: UpdateError(): Error message contains invalid characters");
            return;
        }

        alertAdmins(model.getMessage());
        setLastError(model);
        saveErrorToDatabase(model);
    }

    public void saveErrorToDatabase( ErrorModel model ) {
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
