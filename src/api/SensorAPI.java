package api;

import controller.AccountController;
import controller.SensorController;
import model.SensorModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorAPI extends HttpServlet {

    SensorController sensorController = new SensorController();
    AccountController accountController = new AccountController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        int id=0;
        String time = "";

        // If Zone ID is specified, set ID
        if( contains( req.getQueryString(), "id") ) {
            id = Integer.parseInt(req.getParameter("id"));
            // If ID is out of range, set to 1
            if( id > 8 || id < 1 ) {
                id = 1;
            }
        }

        // If time is specified, set time
        if( contains( req.getQueryString(), "time") ) {
            time = req.getParameter("time");
            if( !time.equals("d") && !time.equals("m") && !time.equals("y") && !time.equals("a") ) {
                time = "";
            }
        }

        // If parameters aren't set, return latest value for each zone
        if( id == 0 && time == "" ) {
            resp.getWriter().println( sensorController.getLatestSensorData() );
        } else {
            resp.getWriter().println( sensorController.getSensorData(id, time));
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        // Convert request parameters to JSONArray
        JSONArray jsonArr = requestParamsToJSON(req);

        ArrayList<SensorModel> sensorList = new ArrayList<>();

        // Verify user identity
        String user = "";
        String APIkey = "";
        try {
            user = req.getSession().getAttribute("user").toString();

        } catch( NullPointerException e ) {
            // If null, do nothing
        }

        try {
            APIkey = req.getParameter("apikey");
        } catch( NullPointerException e ) {
            APIkey = "";
        }

        if( user.equals("admin") || accountController.verifyAPIKey(APIkey) ) {
            try {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject obj = jsonArr.getJSONObject(i);

                    //System.out.println("Light: " + Double.parseDouble(obj.get("light").toString()) + "\nTemperature: " + Double.parseDouble(obj.get("temperature").toString()) + "\nHumidity: " + Double.parseDouble(obj.get("humidity").toString()) + "\nProbe 1:" + Double.parseDouble(obj.get("probe1").toString()) + "\nProbe 2:" + Double.parseDouble(obj.get("probe2").toString()));

                    // Only create new sensor entry if zone has a value change. If all values are null, don't add sensor
                    if (!obj.get("light").equals("") && !obj.get("temperature").equals("") && !obj.get("humidity").equals("") && !obj.get("probe1").equals("") && !obj.get("probe2").equals("")) {
                        // Don't add sensor if all values are 0
                        if( !(Double.parseDouble(obj.get("light").toString()) == 0.0 && Double.parseDouble(obj.get("temperature").toString()) == 0.0 && Double.parseDouble(obj.get("humidity").toString()) == 0.0 && Double.parseDouble(obj.get("probe1").toString()) == 0.0 && Double.parseDouble(obj.get("probe2").toString()) == 0.0) ) {

                            SensorModel sensor = new SensorModel();
                            SensorModel lastSensor = sensorController.getLatestSensorDataByID(i + 1);

                            // If value is null, use value from last reading
                            try {
                                sensor.setLight(Double.parseDouble(obj.get("light").toString()));
                            } catch (NumberFormatException e) {
                                sensor.setLight(lastSensor.getLight());
                            }

                            try {
                                sensor.setTemperature(Double.parseDouble(obj.get("temperature").toString()));
                            } catch (NumberFormatException e) {
                                sensor.setTemperature(lastSensor.getTemperature());
                            }

                            try {
                                sensor.setHumidity(Double.parseDouble(obj.get("humidity").toString()));
                            } catch (NumberFormatException e) {
                                sensor.setHumidity(lastSensor.getHumidity());
                            }

                            try {
                                sensor.setProbe1(Double.parseDouble(obj.get("probe1").toString()));
                            } catch (NumberFormatException e) {
                                sensor.setProbe1(lastSensor.getProbe1());
                            }

                            try {
                                sensor.setProbe2(Double.parseDouble(obj.get("probe2").toString()));
                            } catch (NumberFormatException e) {
                                sensor.setProbe2(lastSensor.getProbe2());
                            }

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date utcTime = new Date();
                            Date date = new Date(utcTime.getTime() + TimeZone.getTimeZone("EST").getRawOffset());
                            sensor.setSampleTime(dateFormat.format(date));

                            sensor.setZone(Integer.parseInt(obj.get("zone").toString()));

                            sensorList.add(sensor);
                        } else {
                            //System.out.println("Blank values. Ignoring");
                        }
                   }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Update latest sensor values
            sensorController.updateLatestSensor( sensorList );

            sensorController.addSensorData(sensorList);

            resp.getWriter().println(sensorController.getLatestSensorData());
        } else {
            resp.getWriter().println("Invalid credentials");
        }

        resp.getWriter().println(sensorController.getLatestSensorData());
    }

    // Check if String is contained within other String
    public boolean contains( String haystack, String needle ) {
        if(needle.equals(""))
            return true;
        if(haystack == null || needle == null || haystack .equals(""))
            return false;

        Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
        Matcher m = p.matcher(haystack);
        return m.find();
    }

    public JSONArray requestParamsToJSON(HttpServletRequest req) {

        JSONArray jsonArr = new JSONArray();

        try {
            Enumeration<String> parameterNames = req.getParameterNames();

            while( parameterNames.hasMoreElements() ) {
                String param = parameterNames.nextElement();

                // Look for data param
                if( param.contains("data") ) {
                    JSONArray arr = new JSONArray(req.getParameter(param));

                    for( int i=0; i<arr.length(); i++ ) {
                        jsonArr.put(arr.get(i));
                    }
                }
            }
        } catch( JSONException e ) {
            e.printStackTrace();
        }

        return jsonArr;
    }

    public int getDataNumber( String param ) {

        param = param.replaceAll("\\D+", "");

        try{
            return Integer.parseInt(param);
        } catch( NullPointerException e ) {
            return 0;
        } catch( NumberFormatException e ) {
            return 0;
        }
    }
}
