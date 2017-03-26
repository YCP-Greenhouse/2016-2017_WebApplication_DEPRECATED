package controller;

import model.AutomationModel;
import model.ScheduleModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
                obj.put("TempLow", rs.getString(2));
                obj.put("TempHigh", rs.getString(3));
                obj.put("moisture", rs.getString(4));
                obj.put("humidity", rs.getString(5));
                obj.put("ShadeLim", rs.getString(6));
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

        // Get next water starts
        obj = getNextWaterSchedules(obj);

        // Get next light starts
        obj = getNextLightSchedules(obj);


        /*
        // Get schedule entries
        HashMap<Integer, ScheduleModel> lightSchedule = getLightSchedule();
        HashMap<Integer, ScheduleModel> waterSchedule = getWaterSchedule();

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

                water.put(waterObj);
            }

            obj.put( "waterschedule", water );

        } catch( JSONException e ) {

        }
        */

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

        // Get schedule entries
        HashMap<Integer, ScheduleModel> currentLightSchedule = getLightSchedule();
        HashMap<Integer, ScheduleModel> currentWaterSchedule = getWaterSchedule();

        // Iterate through current schedule entries, if schedule ID exists update it, otherwise add it
        ArrayList<ScheduleModel> newLightSchedule = automationModel.getLightSchedule();
        for( int i=0; i<newLightSchedule.size(); i++ ) {
            if( currentLightSchedule.containsKey(newLightSchedule.get(i).getId())) {
                updateScheduleEntry("lightschedule", newLightSchedule.get(i) );
            } else {
                addSchedule( "lightschedule", newLightSchedule.get(i) );
                currentLightSchedule.put( newLightSchedule.get(i).getId(), newLightSchedule.get(i) );
            }
        }

        ArrayList<ScheduleModel> newWaterSchedule = automationModel.getWaterSchedule();
        for( int i=0; i<newWaterSchedule.size(); i++ ) {
            if( currentWaterSchedule.containsKey(newWaterSchedule.get(i).getId())) {
                updateScheduleEntry("waterschedule", newWaterSchedule.get(i) );
            } else {
                addSchedule( "waterschedule", newWaterSchedule.get(i) );
                currentWaterSchedule.put( newWaterSchedule.get(i).getId(), newWaterSchedule.get(i) );
            }
        }

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

    public void addSchedule(String database, ScheduleModel newSchedule ) {
        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "INSERT INTO " + database + " VALUES(?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);

            ps.setInt(1, newSchedule.getId() );
            ps.setInt(2, newSchedule.getZoneID() );
            ps.setString(3, newSchedule.getStartTime() );
            ps.setString(4, newSchedule.getEndTime() );
            ps.setDouble(5, newSchedule.getHours() );

            ps.executeUpdate();
            ps.close();

            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }
    }

    public void updateScheduleEntry(String database, ScheduleModel newSchedule ) {

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "UPDATE " + database + " SET zoneId='" + newSchedule.getZoneID() + "', startTime='" + newSchedule.getStartTime() + "', endTime='" + newSchedule.getEndTime() + "', hours='" + newSchedule.getHours() + "' WHERE id='" + newSchedule.getId() + "'";
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

    // Key: Schedule ID
    // Value: Schedule object
    public HashMap<Integer, ScheduleModel> getLightSchedule() {
        HashMap<Integer, ScheduleModel> lightSchedule = new HashMap<>();

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

        String sql = "SELECT * FROM lightschedule";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                ScheduleModel schedule = new ScheduleModel();

                schedule.setId(rs.getInt(1));
                schedule.setZoneID(rs.getInt(2));
                schedule.setStartTime(rs.getString(3));
                schedule.setEndTime(rs.getString(4));
                schedule.setHours(rs.getInt(5));

                lightSchedule.put(schedule.getId(), schedule);
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        return lightSchedule;
    }

    // Key: Schedule ID
    // Value: Schedule object
    public HashMap<Integer, ScheduleModel> getWaterSchedule() {
        HashMap<Integer, ScheduleModel> waterSchedule = new HashMap<>();

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

        String sql = "SELECT * FROM waterschedule";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                ScheduleModel schedule = new ScheduleModel();

                schedule.setId(rs.getInt(1));
                schedule.setZoneID(rs.getInt(2));
                schedule.setStartTime(rs.getString(3));
                schedule.setEndTime(rs.getString(4));
                schedule.setHours(rs.getInt(5));

                waterSchedule.put(schedule.getId(), schedule);
            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        return waterSchedule;
    }

    public JSONObject getNextWaterSchedules( JSONObject obj ) {
        HashMap<Integer, ScheduleModel> waterSchedule = getWaterSchedule();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // Key: Zone, Value: Schedule object
        HashMap<Integer, ScheduleModel > nextWaterStarts = new HashMap<>();

        try {

            // Iterate through water schedule to find next entries for each zone
            for (HashMap.Entry<Integer, ScheduleModel> schedule : waterSchedule.entrySet()) {
                ScheduleModel evalSchedule = schedule.getValue();

                // If nextWaterStarts doesn't contain key, add entry
                if (!nextWaterStarts.containsKey(evalSchedule.getZoneID())) {
                    nextWaterStarts.put(evalSchedule.getZoneID(), evalSchedule);

                    // Else, check if start time is earlier than current start time
                } else {
                    Date scheduleStartTime = dateFormat.parse(evalSchedule.getStartTime());
                    Date currentStartTime = dateFormat.parse(nextWaterStarts.get(evalSchedule.getZoneID()).getStartTime());

                    // If currentStartTime comes after scheduleStartTime, set schedule as next
                    if( currentStartTime.after(scheduleStartTime) ) {
                        nextWaterStarts.remove( evalSchedule.getZoneID() );
                        nextWaterStarts.put( evalSchedule.getZoneID(), evalSchedule );
                    }
                }
            }

            // Add nextWaterStarts to JSON Object
            JSONObject startObj = new JSONObject();
            JSONObject endObj = new JSONObject();
            for( HashMap.Entry<Integer, ScheduleModel> schedule : nextWaterStarts.entrySet() ) {
                ScheduleModel evalSchedule = schedule.getValue();

                String zoneID = Integer.toString(evalSchedule.getZoneID());
                startObj.put( zoneID , evalSchedule.getStartTime() );
                endObj.put( zoneID, evalSchedule.getEndTime() );
            }

            obj.put("WaterStarts", startObj);
            obj.put("WaterEnds", endObj);

        } catch( ParseException e ) {

        } catch( JSONException e ) {

        }


        return obj;
    }

    public JSONObject getNextLightSchedules( JSONObject obj ) {
        HashMap<Integer, ScheduleModel> lightSchedule = getLightSchedule();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // Key: Zone, Value: Schedule object
        HashMap<Integer, ScheduleModel > nextLightStarts = new HashMap<>();

        try {

            // Iterate through water schedule to find next entries for each zone
            for (HashMap.Entry<Integer, ScheduleModel> schedule : lightSchedule.entrySet()) {
                ScheduleModel evalSchedule = schedule.getValue();

                // If nextLightStarts doesn't contain key, add entry
                if (!nextLightStarts.containsKey(evalSchedule.getZoneID())) {
                    nextLightStarts.put(evalSchedule.getZoneID(), evalSchedule);

                    // Else, check if start time is earlier than current start time
                } else {
                    Date scheduleStartTime = dateFormat.parse(evalSchedule.getStartTime());
                    Date currentStartTime = dateFormat.parse(nextLightStarts.get(evalSchedule.getZoneID()).getStartTime());

                    // If currentStartTime comes after scheduleStartTime, set schedule as next
                    if( currentStartTime.after(scheduleStartTime) ) {
                        nextLightStarts.remove( evalSchedule.getZoneID() );
                        nextLightStarts.put( evalSchedule.getZoneID(), evalSchedule );
                    }
                }
            }

            // Add nextLightStarts to JSON Object
            JSONObject startObj = new JSONObject();
            JSONObject endObj = new JSONObject();
            for( HashMap.Entry<Integer, ScheduleModel> schedule : nextLightStarts.entrySet() ) {
                ScheduleModel evalSchedule = schedule.getValue();

                String zoneID = Integer.toString(evalSchedule.getZoneID());
                startObj.put( zoneID , evalSchedule.getStartTime() );
                endObj.put( zoneID, evalSchedule.getEndTime() );
            }

            obj.put("LightStarts", startObj);
            obj.put("LightEnds", endObj);

        } catch( ParseException e ) {

        } catch( JSONException e ) {

        }


        return obj;
    }
}
