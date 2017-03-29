package controller;

import model.ScheduleModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class ScheduleController {

    AutomationController automationController = new AutomationController();
    DatabaseController databaseController = new DatabaseController();

    public JSONObject getSchedules() {

        // Get schedule entries
        HashMap<Integer, ScheduleModel> lightSchedule = automationController.getLightSchedule();
        HashMap<Integer, ScheduleModel> waterSchedule = automationController.getWaterSchedule();

        JSONObject obj = new JSONObject();

        try {

            // Create JSON array of schedule entries
            JSONArray light = new JSONArray();
            for (int i = 0; i < lightSchedule.size(); i++) {
                JSONObject lightObj = new JSONObject();

                lightObj.put( "id", lightSchedule.get(i).getId() );
                lightObj.put( "zone", lightSchedule.get(i).getZoneID() );
                lightObj.put( "start", lightSchedule.get(i).getStartTime() );
                lightObj.put( "end", lightSchedule.get(i).getEndTime() );
                lightObj.put( "hours", lightSchedule.get(i).getHours() );
                lightObj.put( "inverse", lightSchedule.get(i).getInverse() );

                light.put(lightObj);
            }

            obj.put( "lightschedule", light );

            // Create JSON array of schedule entries
            JSONArray water = new JSONArray();
            for (int i = 0; i < waterSchedule.size(); i++) {
                JSONObject waterObj = new JSONObject();

                waterObj.put( "id", waterSchedule.get(i).getId() );
                waterObj.put( "zone", waterSchedule.get(i).getZoneID() );
                waterObj.put( "start", waterSchedule.get(i).getStartTime() );
                waterObj.put( "end", waterSchedule.get(i).getEndTime() );
                waterObj.put( "hours", waterSchedule.get(i).getHours() );
                waterObj.put( "inverse", waterSchedule.get(i).getInverse() );

                water.put(waterObj);
            }

            obj.put( "waterschedule", water );

        } catch( JSONException e ) {

        }

        return obj;
    }

    public void addSchedule(String type, ScheduleModel schedule) {

        String database = "";
        int listSize = 0;

        if( type.equals("light") ) {
            database = "lightschedule";
            HashMap<Integer, ScheduleModel> lightSchedule = automationController.getLightSchedule();
            listSize = lightSchedule.size();

        } else if( type.equals("water") ) {
            database = "waterschedule";
            HashMap<Integer, ScheduleModel> waterSchedule = automationController.getWaterSchedule();
            listSize = waterSchedule.size();

        } else {
            return;
        }

        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "INSERT INTO " + database + " VALUES(?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);

            ps.setInt(1, listSize);
            ps.setInt(2, schedule.getZoneID());
            ps.setString( 3, schedule.getStartTime() );
            ps.setString(4, schedule.getEndTime() );
            ps.setDouble(5, schedule.getHours());
            ps.setInt(6, schedule.getInverse());

            ps.executeUpdate();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }
    }

    public void updateSchedule(String type, ScheduleModel schedule) {
        String database = "";
        if( type.equals("light") ) {
            database = "lightschedule";

        } else if( type.equals("water") ) {
            database = "waterschedule";
        } else {
            return;
        }

        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "UPDATE " + database + " SET zoneId='" + schedule.getZoneID() + "', startTime='" + schedule.getStartTime() + "', endTime='" + schedule.getEndTime() + "', hours='" + schedule.getHours() + "', inverse='" + schedule.getInverse() + "' WHERE id='" + schedule.getId() + "'";

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
