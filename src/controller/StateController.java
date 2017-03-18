package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import model.StateModel;

public class StateController {

    DatabaseController dbController = new DatabaseController();
    StateModel stateModel = new StateModel();

    public StateController() {
        stateModel = getStoredState();

    }

    // Gets previous state from database
    // Used when server starts
    public StateModel getStoredState() {

        // Get website information from DB
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM state";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                stateModel.setLights(stringToBoolean(rs.getString(2)));
                stateModel.setHeaters(stringToBoolean(rs.getString(3)));
                stateModel.setWaterPump(stringToBoolean(rs.getString(4)));
                stateModel.setFans(stringToBoolean(rs.getString(5)));
                stateModel.setVents(stringToBoolean(rs.getString(6)));
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        return stateModel;
    }

    // Set state
    public void setCurrentState(boolean lights, boolean heaters, boolean waterPump, boolean fans, boolean vents, boolean shades) {
        stateModel.setLights(lights);
        stateModel.setHeaters(heaters);
        stateModel.setWaterPump(waterPump);
        stateModel.setFans(fans);
        stateModel.setVents(vents);
        stateModel.setShades(shades);
    }

    public void setCurrentState(StateModel stateModel) {
        this.stateModel = stateModel;
    }

    // Save state variables to database
    public void storeCurrentState() {
        // Get website information from DB
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;

        String sql = "UPDATE state SET lights='" + booleanToNum(stateModel.getLights()) + "', heaters='" + booleanToNum(stateModel.getHeaters()) + "', waterPumps='" + booleanToNum(stateModel.getWaterPump()) + "', fans='" + booleanToNum(stateModel.getFans()) + "', vents='" + booleanToNum(stateModel.getVents()) + "', shades='" + booleanToNum(stateModel.getShades()) + "' WHERE id='1'";

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

    // Get current state from StateModel
    public StateModel getCurrentState() {
        return stateModel;
    }

    // Return current state as JSON Object
    public JSONObject getCurrentStateJSON() {

        stateModel = getCurrentState();
        JSONObject obj = new JSONObject();

        //System.out.println("\n\nGet Current State: \nLights: " + stateModel.getLights() + "\nFans: " + stateModel.getFans() + "\nPump: " + stateModel.getWaterPump() + "\nHeater: " + stateModel.getHeaters());


        try {
            // Set JSON object fields
            obj.put("lights", stateModel.getLights());
            obj.put("fans", stateModel.getFans());
            obj.put("pump", stateModel.getWaterPump());
            obj.put("heater", stateModel.getHeaters());
            obj.put("vents", stateModel.getVents());
            obj.put("shades", stateModel.getShades());

        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return obj;
    }

    // Converts 1's and 0's from database into 'true' and 'false'
    public boolean stringToBoolean(String input) {

        if( input.contains("1") ) {
            return true;
        } else {
            return false;
        }
    }

    public String booleanToOnOff(boolean input) {


        if( input ) {
            return "On";
        } else {
            return "Off";
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
