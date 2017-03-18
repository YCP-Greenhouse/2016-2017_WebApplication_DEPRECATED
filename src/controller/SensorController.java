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

    // Categorize sensors into arrays by zone
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
                sensorModel.setMoisture(Double.parseDouble(rs.getString(3)));
                sensorModel.setTemperature(Double.parseDouble(rs.getString(4)));
                sensorModel.setLight(Double.parseDouble(rs.getString(5)));
                sensorModel.setHumidity(Double.parseDouble(rs.getString(6)));

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = dateFormat.parse(rs.getString(7));

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

    public JSONArray getLatestSensorData() {

        HashMap<Integer, ArrayList<SensorModel>> sensorMap = categorizeSensorData();

        JSONArray jsonData = new JSONArray();

        try {
            // Iterate through all values in hashmap to find the most recent entry for each zone
            for (HashMap.Entry<Integer, ArrayList<SensorModel>> zone : sensorMap.entrySet()) {
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
                obj.put("moisture", sensorList.get(latestKey).getMoisture() );
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

        return jsonData;
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
            sensor.setMoisture(zoneList.get(latestKey).getMoisture());
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
                            obj.put("moisture", sensorEntry.getMoisture() );
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
                        obj.put("moisture", sensor.getMoisture() );
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
                    obj.put("moisture", sensorList.get(latestKey).getMoisture() );
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
                avgMoisture += Double.parseDouble(latestData.getJSONObject(i).get("moisture").toString());
            }

            avgLight = avgLight / latestData.length();
            avgHumidity = avgHumidity / latestData.length();
            avgTemperature = avgTemperature / latestData.length();
            avgMoisture = avgMoisture / latestData.length();

            jsonData.put("light",  avgLight );
            jsonData.put("humidity", avgHumidity );
            jsonData.put("temperature", avgTemperature);
            jsonData.put("moisture", avgMoisture);

            System.out.println(jsonData);

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

        // Connect to database
        Connection conn = null;
        try {
            conn = dbController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        PreparedStatement ps = null;
        String sql = "INSERT INTO sensordata VALUES(?,?,?,?,?,?,?)";

        try {

            int avgLight = 0;
            double avgHumidity = 0.0;
            double avgTemperature = 0.0;
            double avgMoisture = 0.0;

            conn.setAutoCommit(false);

            // Enter all entries in ArrayList into database
            for( SensorModel sensor : sensorList ) {
                ps = conn.prepareStatement(sql);

                System.out.println("Add Sensors:\nZone: " + sensor.getZone() + "\nMoisture: " + sensor.getMoisture() + "\nTemperature: " + sensor.getTemperature() + "\nLight: " + sensor.getLight() + "\nHumidity: " + sensor.getHumidity());

                ps.setString(1, null );
                ps.setInt(2, sensor.getZone() );
                ps.setDouble(3, sensor.getMoisture() );
                ps.setDouble(4, sensor.getTemperature() );
                ps.setDouble( 5, sensor.getLight() );
                ps.setDouble( 6, sensor.getHumidity() );
                ps.setString( 7, sensor.getSampleTime() );

                avgLight += sensor.getLight();
                avgHumidity += sensor.getHumidity();
                avgTemperature += sensor.getTemperature();
                avgMoisture += sensor.getMoisture();

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
