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
import java.util.*;

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


        try {
            obj.put("WaterSchedules", getNextSchedules("water"));
            obj.put("LightSchedules", getNextSchedules("light"));

        } catch( JSONException e ) {

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
        //System.out.println(sql);

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
                schedule.setDay(rs.getInt(3));
                schedule.setStartTime(rs.getString(4));
                schedule.setEndTime(rs.getString(5));
                schedule.setHours(rs.getDouble(6));
                schedule.setType(rs.getString(7));
                schedule.setThreshold(rs.getInt(8));

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
                schedule.setDay(rs.getInt(3));
                schedule.setStartTime(rs.getString(4));
                schedule.setEndTime(rs.getString(5));
                schedule.setHours(rs.getDouble(6));
                schedule.setType(rs.getString(7));
                schedule.setThreshold(rs.getInt(8));

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

    // Returns JSONArray of schedule events
    public JSONArray getNextSchedules(String type) {
        HashMap<Integer, ScheduleModel> schedule = new HashMap<>();

        // Key: Zone, Value: Schedule object
        HashMap<Integer, ScheduleModel> nextStarts = new HashMap<>();

        if( type.equals("light") ) {
            schedule = getLightSchedule();
        } else if( type.equals("water") ) {
            schedule = getWaterSchedule();
        } else {
            return null;
        }

        // Get today as day of the week 0-6
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();

        try {
            now = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dbController.getCurrentTime());
            calendar.setTime(now);
        } catch( ParseException e ) {
            System.out.println("Time format error");
            return null;
        }

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek--; // Decrement dayOfWeek to coincide with (0-6) day standard

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        JSONArray arr = new JSONArray();

        try {

            for( int i=1; i<=6; i++ ) {
                for (HashMap.Entry<Integer, ScheduleModel> s : schedule.entrySet()) {
                    ScheduleModel evalSchedule = s.getValue();

                    if (evalSchedule.getZoneID() == i) {
                        // If schedule day matches today, look for next start time
                        if (evalSchedule.getDay() == dayOfWeek && Integer.parseInt(evalSchedule.getEndTime().split(":")[0]) >= currentHour) {

                            // If zone ID doesn't have entry, add current schedule
                            if (!nextStarts.containsKey(evalSchedule.getZoneID())) {
                                nextStarts.put(i, evalSchedule);

                                // Else evaluate if current schedule occurs before nextWaterStarts schedule
                            } else {
                                if (Integer.parseInt(evalSchedule.getStartTime().split(":")[0]) < Integer.parseInt(nextStarts.get(i).getStartTime().split(":")[0])) {
                                    nextStarts.remove(i);
                                    nextStarts.put(i, evalSchedule);
                                }
                            }
                        }
                    }
                }

                // Create JSON Object from hash map of next schedules
                if( nextStarts.containsKey(i) ) {
                    JSONObject obj = new JSONObject();
                    ScheduleModel nextSchedule = nextStarts.get(i);

                    obj.put("zone", nextSchedule.getZoneID());
                    obj.put("start", nextSchedule.getStartTime());
                    obj.put("end", nextSchedule.getEndTime());
                    obj.put("type", nextSchedule.getType());

                    if( nextSchedule.getType().equals("sensors")) {
                        obj.put("threshold", nextSchedule.getThreshold());
                    }

                    // Add obj to JSON Array
                    arr.put(obj);
                }
            }

        } catch( JSONException e ) {

        }

        return arr;
    }
}
