package controller;

import model.AutomationModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kylemusco on 1/28/17.
 */
public class AutomationController {

    DatabaseController dbController = new DatabaseController();

    public JSONObject getAutomationSettings() {

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM automation";

        JSONObject obj = new JSONObject();

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {


                obj.put("temperature", rs.getString(2));
                obj.put("moisture", rs.getString(3));
                obj.put("humidity", rs.getString(4));
                obj.put("light", rs.getString(5));

            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        } catch( JSONException e ) {
            e.printStackTrace();
            return null;
        }

        return obj;
    }

    public void updateAutomationSettings(AutomationModel automationModel) {

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "UPDATE automation SET temperature='" + automationModel.getTemperature() + "', moisture='" + automationModel.getSoilMoisture() + "', humidity='" + automationModel.getHumidity() + "', light='" + automationModel.getLightIntesity() + "' WHERE id='1'";

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
}
