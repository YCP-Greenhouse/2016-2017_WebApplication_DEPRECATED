package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeatherController {
    DatabaseController dbController = new DatabaseController();

    public JSONArray getWeatherData() {
        JSONArray arr = new JSONArray();

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

        String sql = "SELECT * FROM weather";

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while( rs.next() ) {
                JSONObject obj = new JSONObject();

                obj.put("temp", rs.getString(2));
                obj.put("high", rs.getString(3));
                obj.put("low", rs.getString(4));
                obj.put("sampletime", rs.getString(5));

                arr.put(obj);
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

        return arr;
    }

}
