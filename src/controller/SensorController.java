package controller;

import model.SensorModel;
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

public class SensorController {

    DatabaseController dbController = new DatabaseController();
    HashMap<Integer, SensorModel> latestSensors = new HashMap<>();
    int min = -1;

    public JSONArray getAllSensorDataJSON() {

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

        String sql = "SELECT * FROM sensordata";

        JSONArray jsonData = new JSONArray();

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                JSONObject obj = new JSONObject();

                //obj.put("id", rs.getString(1));
                obj.put("zone", rs.getString(2));
                obj.put("moisture", rs.getString(3));
                obj.put("temperature", rs.getString(4));
                obj.put("light", rs.getString(5));
                obj.put("humidity", rs.getString(6));
                obj.put("sampletime", rs.getString(7));

                jsonData.put(obj);

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


        return jsonData;
    }

    // Get all entries and categorize sensors into arrays by zone
    // Key: zone
    // Value: ArrayList of sensor data
    public HashMap<Integer, ArrayList<SensorModel>> categorizeSensorData() {

        HashMap<Integer, ArrayList<SensorModel>> sensorMap = new HashMap<>();

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

        String sql = "SELECT * FROM sensordata";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {

                ArrayList<SensorModel> sensorList = new ArrayList<>();

                // Get value from HashMap
                if( sensorMap.containsKey(Integer.parseInt(rs.getString(2)) ) ) {
                    sensorList = sensorMap.get(Integer.parseInt(rs.getString(2)));
                } else {
                    sensorList = new ArrayList<>();
                }

                SensorModel sensorModel = new SensorModel();

                sensorModel.setEntryId(Integer.parseInt(rs.getString(1)));
                sensorModel.setZone(Integer.parseInt(rs.getString(2)));
                sensorModel.setProbe1(rs.getDouble(3));
                sensorModel.setProbe2(rs.getDouble(4));
                sensorModel.setTemperature(Double.parseDouble(rs.getString(5)));
                sensorModel.setLight(Double.parseDouble(rs.getString(6)));
                sensorModel.setHumidity(Double.parseDouble(rs.getString(7)));

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(rs.getString(8));

                sensorModel.setSampleTime( dateFormat.format(date) );

                sensorList.add(sensorModel);

                sensorMap.put(Integer.parseInt(rs.getString(2)), sensorList);

            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        } catch( ParseException e ) {
            e.printStackTrace();
            return null;
        }

        return sensorMap;

    }

    public HashMap<Integer, ArrayList<SensorModel>> categorizeTodaysSensorData() {
        HashMap<Integer, ArrayList<SensorModel>> sensorMap = new HashMap<>();

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

        String time = dbController.getCurrentTime();
        String[] timeArr = time.split(" ");



        String sql = "SELECT * FROM sensordata WHERE sampleTime>" + timeArr[0];

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {

                ArrayList<SensorModel> sensorList = new ArrayList<>();

                // Get value from HashMap
                if( sensorMap.containsKey(Integer.parseInt(rs.getString(2)) ) ) {
                    sensorList = sensorMap.get(Integer.parseInt(rs.getString(2)));
                } else {
                    sensorList = new ArrayList<>();
                }

                SensorModel sensorModel = new SensorModel();

                //System.out.println("categorizeTodaysSensorData: EntryID: " + Integer.parseInt(rs.getString(1)) );

                sensorModel.setEntryId(Integer.parseInt(rs.getString(1)));
                sensorModel.setZone(Integer.parseInt(rs.getString(2)));
                sensorModel.setProbe1(rs.getDouble(3));
                sensorModel.setProbe2(rs.getDouble(4));
                sensorModel.setTemperature(Double.parseDouble(rs.getString(5)));
                sensorModel.setLight(Double.parseDouble(rs.getString(6)));
                sensorModel.setHumidity(Double.parseDouble(rs.getString(7)));

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(rs.getString(8));

                sensorModel.setSampleTime( dateFormat.format(date) );

                sensorList.add(sensorModel);

                sensorMap.put(Integer.parseInt(rs.getString(2)), sensorList);

            }

            rs.close();
            ps.close();
            conn.commit();
            conn.close();

        } catch( SQLException e ) {
            e.printStackTrace();
            return null;
        } catch( ParseException e ) {
            e.printStackTrace();
            return null;
        }

        return sensorMap;
    }

    public void loadLatestSensorValues() {
        HashMap<Integer, ArrayList<SensorModel>> latestList = categorizeTodaysSensorData();

        try {
            for (HashMap.Entry<Integer, ArrayList<SensorModel>> zone : latestList.entrySet()) {
                int key = zone.getKey();
                ArrayList<SensorModel> sensorList = zone.getValue();

                // Set first entry as starting date
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date latestDate = dateFormat.parse(sensorList.get(0).getSampleTime());

                // Set first entry key as "last entry" key
                int latestKey = 0;
                int count = 0;

                // Iterate through ArrayList to find last entry
                for (SensorModel sensorEntry : sensorList) {
                    Date sampleDate = dateFormat.parse(sensorEntry.getSampleTime());
                    // If sampleDate is more recent than date, set
                    if (sampleDate.after(latestDate)) {
                        latestDate = sampleDate;
                        latestKey = count;
                    }

                    count++;
                }

                SensorModel sensor = new SensorModel();

                sensor.setZone(key);
                sensor.setProbe1(sensorList.get(latestKey).getProbe1());
                sensor.setProbe2(sensorList.get(latestKey).getProbe2());
                sensor.setHumidity(sensorList.get(latestKey).getHumidity());
                sensor.setTemperature(sensorList.get(latestKey).getTemperature());
                sensor.setLight(sensorList.get(latestKey).getLight());
                sensor.setSampleTime(sensorList.get(latestKey).getSampleTime());

                latestSensors.put(key, sensor);
            }

        } catch( ParseException e ) {

        }

        return;
    }

    public void updateLatestSensor(ArrayList<SensorModel> sensorList) {

        for( SensorModel sensor : sensorList) {
            // Remove old value
            latestSensors.remove(sensor.getZone());

            // Add new value
            latestSensors.put( sensor.getZone(), sensor );
        }

        return;
    }

    public JSONArray getLatestSensorData() {

        if( latestSensors.isEmpty() ) {
            loadLatestSensorValues();
        }

        JSONArray jsonData = new JSONArray();

        try {
            for( HashMap.Entry<Integer, SensorModel> sensorEntry: latestSensors.entrySet() ) {
                SensorModel sensor = sensorEntry.getValue();

                JSONObject obj = new JSONObject();

                obj.put("zone", sensor.getZone() );
                obj.put("probe1", sensor.getProbe1());
                obj.put("probe2", sensor.getProbe2());
                obj.put("temperature", sensor.getTemperature() );
                obj.put("light", sensor.getLight() );
                obj.put("humidity", sensor.getHumidity() );
                obj.put("sampletime", sensor.getSampleTime() );

                jsonData.put(obj);
            }
        } catch( JSONException e ) {

        }

        return jsonData;

        /*
        HashMap<Integer, ArrayList<SensorModel>> latestList = categorizeLimitedSensorData();

        JSONArray jsonData = new JSONArray();

        try {
            // Iterate through all values in hashmap to find the most recent entry for each zone
            for (HashMap.Entry<Integer, ArrayList<SensorModel>> zone : latestList.entrySet()) {
                int key = zone.getKey();
                ArrayList<SensorModel> sensorList = zone.getValue();

                // Set first entry as starting date
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date latestDate = dateFormat.parse(sensorList.get(0).getSampleTime());

                // Set first entry key as "last entry" key
                int latestKey = 0;
                int count = 0;

                // Iterate through ArrayList to find last entry
                for (SensorModel sensorEntry : sensorList) {
                    Date sampleDate = dateFormat.parse(sensorEntry.getSampleTime());
                    // If sampleDate is more recent than date, set
                    if( sampleDate.after(latestDate) ) {
                        latestDate = sampleDate;
                        latestKey = count;
                    }

                    count++;
                }

                // Add last entry to JSONArray
                JSONObject obj = new JSONObject();

                obj.put("zone", sensorList.get(latestKey).getZone() );
                obj.put("probe1", sensorList.get(latestKey).getProbe1());
                obj.put("probe2", sensorList.get(latestKey).getProbe2());
                obj.put("temperature", sensorList.get(latestKey).getTemperature() );
                obj.put("light", sensorList.get(latestKey).getLight() );
                obj.put("humidity", sensorList.get(latestKey).getHumidity() );
                obj.put("sampletime", sensorList.get(latestKey).getSampleTime() );

                jsonData.put(obj);

            }
        } catch( ParseException e ) {
            e.printStackTrace();
        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return jsonData;*/
    }

    public SensorModel getLatestSensorDataByID(int zoneID) {

        HashMap<Integer, ArrayList<SensorModel>> sensorMap = categorizeSensorData();

        SensorModel sensor = new SensorModel();

        try {
            ArrayList<SensorModel> zoneList = sensorMap.get(zoneID);

            // Set first entry as starting date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date latestDate = dateFormat.parse(zoneList.get(0).getSampleTime());

            // Set first entry key as "last entry" key
            int latestKey = 0;
            int count = 0;

            // Iterate through ArrayList to find last entry
            for (SensorModel sensorEntry : zoneList) {
                Date sampleDate = dateFormat.parse(sensorEntry.getSampleTime());
                // If sampleDate is more recent than date, set
                if (sampleDate.after(latestDate)) {
                    latestDate = sampleDate;
                    latestKey = count;
                }

                count++;
            }

            // Set sensor to latest values
            sensor.setZone(zoneID);
            sensor.setProbe1(zoneList.get(latestKey).getProbe1());
            sensor.setProbe2(zoneList.get(latestKey).getProbe2());
            sensor.setTemperature(zoneList.get(latestKey).getTemperature());
            sensor.setLight(zoneList.get(latestKey).getLight());
            sensor.setHumidity(zoneList.get(latestKey).getHumidity());
            sensor.setSampleTime(zoneList.get(latestKey).getSampleTime());
        } catch( ParseException e ) {

        }

        return sensor;
    }

    public JSONArray getSensorData(int id, String time) {

        HashMap<Integer, ArrayList<SensorModel>> sensorMap = categorizeSensorData();
        JSONArray jsonData = new JSONArray();

        // Get current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date utcTime = new Date();
        Date date = new Date(utcTime.getTime() + TimeZone.getTimeZone("EST").getRawOffset() );
        String currentTime = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        try {
            // if ID wasn't set, return all zones for specified time
            if( id == 0) {

                for( HashMap.Entry<Integer, ArrayList<SensorModel>> zone : sensorMap.entrySet() ) {
                    int key = zone.getKey();
                    ArrayList<SensorModel> sensorList = zone.getValue();

                    // Iterate through ArrayList to find entries within desired time range
                    for (SensorModel sensorEntry : sensorList) {
                        int toFind = 0;
                        int sensorTime = -1;

                        Calendar sampleDate = Calendar.getInstance();
                        sampleDate.setTime(dateFormat.parse(sensorEntry.getSampleTime()));

                        if( time.equals("d") ) {
                            toFind = calendar.get(Calendar.DAY_OF_MONTH);
                            sensorTime = sampleDate.get(Calendar.DAY_OF_MONTH);
                        } else if( time.equals("m") ) {
                            toFind = calendar.get(Calendar.MONTH);
                            sensorTime = sampleDate.get(Calendar.MONTH);
                        } else if( time.equals("y")) {
                            toFind = calendar.get(Calendar.YEAR);
                            sensorTime = sampleDate.get(Calendar.YEAR);
                        } else if( time.equals("a")) {
                            toFind = sensorTime = 1 ;
                        }

                        if( toFind == sensorTime ) {
                            JSONObject obj = new JSONObject();

                            obj.put("zone", sensorEntry.getZone() );
                            obj.put("probe1", sensorEntry.getProbe1());
                            obj.put("probe2", sensorEntry.getProbe2());
                            obj.put("temperature", sensorEntry.getTemperature() );
                            obj.put("light", sensorEntry.getLight() );
                            obj.put("humidity", sensorEntry.getHumidity() );
                            obj.put("sampletime", sensorEntry.getSampleTime() );
                            //obj.put("tofind", toFind );
                            //obj.put("sensorTime", sensorTime );

                            jsonData.put(obj);
                        }
                    }

                }

                // If ID is set, find values for that specific id
            } else {

                ArrayList<SensorModel> sensorList = sensorMap.get(id);

                // Set first entry as starting date
                Date latestDate = dateFormat.parse(sensorList.get(1).getSampleTime());

                // Set first entry key as "last entry" key
                int latestKey = 0;
                int count = 0;

                for( SensorModel sensor : sensorList ) {

                    Calendar sampleDate = Calendar.getInstance();
                    sampleDate.setTime(dateFormat.parse(sensor.getSampleTime()));

                    int toFind = 0;
                    int sensorTime = -1;

                    if( time.equals("d") ) {
                        toFind = calendar.get(Calendar.DAY_OF_MONTH);
                        sensorTime = sampleDate.get(Calendar.DAY_OF_MONTH);
                    } else if( time.equals("m") ) {
                        toFind = calendar.get(Calendar.MONTH);
                        sensorTime = sampleDate.get(Calendar.MONTH);
                    } else if( time.equals("y")) {
                        toFind = calendar.get(Calendar.YEAR);
                        sensorTime = sampleDate.get(Calendar.YEAR);
                    } else if( time.equals("a")) {
                        toFind = sensorTime;
                    } else if( time.equals("")) {
                        Date sampleDate2 = dateFormat.parse(sensor.getSampleTime());
                        // If sampleDate is more recent than date, set
                        if( sampleDate2.after(latestDate) ) {
                            latestDate = sampleDate2;
                            latestKey = count;
                        }

                        count++;
                    }

                    if( toFind == sensorTime && time != "") {
                        JSONObject obj = new JSONObject();

                        obj.put("zone", sensor.getZone() );
                        obj.put("probe1", sensor.getProbe1());
                        obj.put("probe2", sensor.getProbe2());
                        obj.put("temperature", sensor.getTemperature() );
                        obj.put("light", sensor.getLight() );
                        obj.put("humidity", sensor.getHumidity() );
                        obj.put("sampletime", sensor.getSampleTime() );

                        jsonData.put(obj);
                    }
                }

                if( time == "" ) {
                    JSONObject obj = new JSONObject();

                    obj.put("zone", sensorList.get(latestKey).getZone() );
                    obj.put("probe1", sensorList.get(latestKey).getProbe1());
                    obj.put("probe2", sensorList.get(latestKey).getProbe2());
                    obj.put("temperature", sensorList.get(latestKey).getTemperature() );
                    obj.put("light", sensorList.get(latestKey).getLight() );
                    obj.put("humidity", sensorList.get(latestKey).getHumidity() );
                    obj.put("sampletime", sensorList.get(latestKey).getSampleTime() );

                    jsonData.put(obj);
                }
            }

        } catch( JSONException e ) {
            e.printStackTrace();
        } catch( ParseException e ) {
            e.printStackTrace();
        }

        return jsonData;
    }

    public JSONObject getCurrentAverages() {

        JSONArray latestData = getLatestSensorData();
        JSONObject jsonData = new JSONObject();

        double avgLight = 0;
        double avgHumidity = 0.0;
        double avgTemperature = 0.0;
        double avgMoisture = 0.0;

        try {
            for (int i = 0; i < latestData.length(); i++) {
                avgLight += Double.parseDouble(latestData.getJSONObject(i).get("light").toString());
                avgHumidity += Double.parseDouble(latestData.getJSONObject(i).get("humidity").toString());
                avgTemperature += Double.parseDouble(latestData.getJSONObject(i).get("temperature").toString());
                avgMoisture += Double.parseDouble(latestData.getJSONObject(i).get("probe1").toString());
                avgMoisture += Double.parseDouble(latestData.getJSONObject(i).get("probe2").toString());
            }

            avgLight = avgLight / latestData.length();
            avgHumidity = avgHumidity / latestData.length();
            avgTemperature = avgTemperature / latestData.length();
            avgMoisture = avgMoisture / ( 2*latestData.length() );

            jsonData.put("light",  avgLight );
            jsonData.put("humidity", avgHumidity );
            jsonData.put("temperature", avgTemperature);
            jsonData.put("moisture", avgMoisture);

            //System.out.println(jsonData);

        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return jsonData;
    }

    public JSONArray getAllAverages() {
        JSONArray jsonData = new JSONArray();

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

        String sql = "SELECT * FROM averages";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                JSONObject obj = new JSONObject();

                obj.put("temperature", rs.getDouble(2));
                obj.put("humidity", rs.getDouble(3));
                obj.put("light", rs.getInt(4));
                obj.put("moisture", rs.getDouble(5));
                obj.put("sampletime", rs.getString(6));

                jsonData.put(obj);
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

        return jsonData;
    }

    // Add ArrayList of SensorModels to database
    public void addSensorData(ArrayList<SensorModel> sensorList) {

        Calendar now = Calendar.getInstance();

        // Only save every 15 minutes
        if( now.get(Calendar.MINUTE) % 15 == 0 ) {
            if( now.get(Calendar.MINUTE) == min ) {
                //System.out.println("Already been saved. Ignoring");
                return;
            } else {
                //System.out.println("Saving entry. Setting min to " + now.get(Calendar.MINUTE) );
                min = now.get(Calendar.MINUTE);
            }
        } else {
            //System.out.println("Not interval of 15. Minutes: " + now.get(Calendar.MINUTE));
            return;
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
        String sql = "INSERT INTO sensordata VALUES(?,?,?,?,?,?,?,?)";

        try {

            int avgLight = 0;
            double avgHumidity = 0.0;
            double avgTemperature = 0.0;
            double avgMoisture = 0.0;

            conn.setAutoCommit(false);

            // Enter all entries in ArrayList into database
            for( SensorModel sensor : sensorList ) {
                ps = conn.prepareStatement(sql);

                //System.out.println("Add Sensors:\nZone: " + sensor.getZone() + "\nMoisture: " + sensor.getMoisture() + "\nTemperature: " + sensor.getTemperature() + "\nLight: " + sensor.getLight() + "\nHumidity: " + sensor.getHumidity());

                ps.setString(1, null );
                ps.setInt(2, sensor.getZone() );
                ps.setDouble(3, sensor.getProbe1() );
                ps.setDouble(4, sensor.getProbe2() );
                ps.setDouble(5, sensor.getTemperature() );
                ps.setDouble( 6, sensor.getLight() );
                ps.setDouble( 7, sensor.getHumidity() );
                ps.setString( 8, sensor.getSampleTime() );

                avgLight += sensor.getLight();
                avgHumidity += sensor.getHumidity();
                avgTemperature += sensor.getTemperature();
                avgMoisture += sensor.getProbe1() + sensor.getProbe2();

                ps.executeUpdate();
                ps.close();
            }

            if( avgLight != 0 && avgHumidity != 0.0 && avgTemperature != 0.0 && avgMoisture != 0.0 && sensorList.size() > 0) {
                sql = "INSERT INTO averages VALUES(?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);

                //System.out.println("Averages:\nLight: " + avgLight + "\nHumidity: " + avgHumidity + "\nTemperature: " + avgTemperature + "\nMoisture: " + avgMoisture + "\nSensorSize: " + sensorList.size() );
                Double aHumidity = avgHumidity / sensorList.size();
                Double aMoisture = avgMoisture / sensorList.size();

                // Check limits
                if( aHumidity > 100.0 ) {
                    aHumidity = 100.0;
                }

                if( aMoisture > 100.0 ) {
                    aMoisture = 100.0;
                }

                ps.setString(1, null);
                ps.setDouble(2, avgTemperature / sensorList.size());
                ps.setDouble(3, aHumidity );
                ps.setInt(4, avgLight / sensorList.size());
                ps.setDouble(5, aMoisture);
                ps.setString(6, dbController.getCurrentTime());

                ps.executeUpdate();
                ps.close();
            }

            conn.commit();
            conn.close();


        } catch( SQLException e ) {
            e.printStackTrace();
            return;
        }

    }

    public int getYear( String date ) {
        return Integer.parseInt( date.substring(0,4) );
    }

    public int getDay( String date ) {
        return Integer.parseInt( date.substring(5,8) );
    }

    public int getMonth( String date ) {
        return Integer.parseInt( date.substring(8,10) );
    }
}
