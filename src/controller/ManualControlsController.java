package controller;

import model.ManualControlsModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManualControlsController {
    DatabaseController databaseController = new DatabaseController();
    ManualControlsModel manualControlsModel = new ManualControlsModel();

    public ManualControlsModel getStoredState() {

        // Get website information from DB
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM manualcontrols";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                manualControlsModel.setLights(toBoolean(rs.getInt(2)));
                manualControlsModel.setShades(toBoolean(rs.getInt(3)));
                manualControlsModel.setFans(toBoolean(rs.getInt(4)));
                manualControlsModel.setWater(toBoolean(rs.getInt(5)));
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        return manualControlsModel;
    }

    public JSONObject getManualControls() {
        manualControlsModel = getStoredState();

        JSONObject obj = new JSONObject();

        try {
            obj.put("lights", manualControlsModel.isLights());
            obj.put("shades", manualControlsModel.isShades());
            obj.put("fans", manualControlsModel.isFans());
            obj.put("water", manualControlsModel.isWater());

        } catch( JSONException e ) {

        }

        return obj;

    }

    public boolean toBoolean(int num) {
        if( num == 1 ) {
            return true;
        }

        return false;
    }

    public void updateControls(ManualControlsModel controls) {
        // Get website information from DB
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;

        String sql = "UPDATE manualcontrols SET light='" + booleanToNum(controls.isLights()) + "', shades='" + booleanToNum(controls.isShades()) + "', fans='" + booleanToNum(controls.isFans()) + "', water='" + booleanToNum(controls.isWater()) + "' WHERE id='1'";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();

            conn.commit();
            conn.close();

        } catch( SQLException e) {
            e.printStackTrace();
        }
    }

    // Convert 'True' 'False' to '1' '0'
    public String booleanToNum(boolean input) {
        if(input) {
            return "1";
        } else {
            return "0";
        }
    }
}
