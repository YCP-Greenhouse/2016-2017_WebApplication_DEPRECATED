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
                obj.put("templow", rs.getString(2));
                obj.put("temphigh", rs.getString(3));
                obj.put("moisture", rs.getString(4));
                obj.put("humidity", rs.getString(5));
                obj.put("light", rs.getString(6));
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

    public AutomationModel getCurrentAutomationSettings() {

        AutomationModel model = new AutomationModel();
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

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                model.setTempLow( Integer.parseInt(rs.getString(2)));
                model.setTempHigh( Integer.parseInt(rs.getString(3)));
                model.setSoilMoisture( Integer.parseInt(rs.getString(4)));
                model.setHumidity( Integer.parseInt(rs.getString(5)));
                model.setLightIntesity( Integer.parseInt(rs.getString(6)));
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        return model;
    }

    public void updateAutomationSettings(AutomationModel automationModel) {

        System.out.println("Temp low: " + automationModel.getTempLow() + "\nTemp high: " + automationModel.getTempHigh() + "\nMoisture: " + automationModel.getSoilMoisture() + "\nHumidity: " + automationModel.getHumidity() + "\nLight: " + automationModel.getLightIntesity());

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "UPDATE automation SET templow='" + automationModel.getTempLow() + "', temphigh='" + automationModel.getTempHigh() + "', moisture='" + automationModel.getSoilMoisture() + "', humidity='" + automationModel.getHumidity() + "', light='" + automationModel.getLightIntesity() + "' WHERE id='1'";

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
