package controller;

import model.ErrorModel;
import model.NotificationModel;
import model.SensorModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationController {
    DatabaseController databaseController = new DatabaseController();
    ErrorController errorController = new ErrorController();

    NotificationModel notificationSettings = new NotificationModel();

    boolean wait = false;

    public void toggleWait() {

        wait = true;


        // Toggle wait variable
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                wait = false;
            }
        }, 30000 );

    }

    public void checkBounds(SensorModel sensor) {

        // Only send error messages every 15 minutes. Return if sent within 15 minutes
        if( wait ) {
            return;
        }

        // Check that notificationSettings is populated
        if( notificationSettings.getSoilHigh() == 0 ) {
            getNotificationSettings();
        }

        ErrorModel error = new ErrorModel();
        error.setTime(databaseController.getCurrentTime());

        // Check if user wants to be notified for temperature thresholds
        if( notificationSettings.getNotifyTemp() == 1 ) {

            // Check if current temperature is outside thresholds
            if( sensor.getTemperature() < notificationSettings.getTempLow() || sensor.getTemperature() > notificationSettings.getTempHigh() ) {
                error.setMessage( "Zone " + sensor.getZone() + " temperature is currently " + sensor.getTemperature() + "°");

                errorController.updateError(error);
                toggleWait();

                return;
            }
        }

        // Check if user wants to be notified for soil moisture thresholds
        if( notificationSettings.getNotifySoil() == 1 ) {

            // Check if current soil moisture is outside thresholds
            if( sensor.getProbe1() < notificationSettings.getSoilLow() || sensor.getProbe1() > notificationSettings.getSoilHigh() ) {
                error.setMessage( "Zone " + sensor.getZone() + " Probe 1 soil moisture is currently " + sensor.getProbe1() + "%");

                errorController.updateError(error);
                toggleWait();

                return;
            }

            if( sensor.getProbe2() < notificationSettings.getSoilLow() || sensor.getProbe2() > notificationSettings.getSoilHigh() ) {
                error.setMessage( "Zone " + sensor.getZone() + " Probe 2 soil moisture is currently " + sensor.getProbe2() + "%");

                errorController.updateError(error);
                toggleWait();

                return;
            }

        }
    }

    public JSONObject getNotificationSettings() {

        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM notifications";

        JSONObject obj = new JSONObject();

        try {
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if( rs.next() ) {
                // Set JSON Object
                obj.put("tempNotify", rs.getInt(2));
                obj.put("tempHigh", rs.getInt(3));
                obj.put("tempLow", rs.getInt(4));
                obj.put("soilNotify", rs.getInt(5));
                obj.put("soilHigh", rs.getInt(6));
                obj.put("soilLow", rs.getInt(7));

                // Set Notification Model
                notificationSettings.setNotifyTemp(rs.getInt(2));
                notificationSettings.setTempHigh(rs.getInt(3));
                notificationSettings.setTempLow(rs.getInt(4));
                notificationSettings.setNotifySoil(rs.getInt(5));
                notificationSettings.setSoilHigh(rs.getInt(6));
                notificationSettings.setSoilLow(rs.getInt(7));
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

    public void updateNotifications(NotificationModel notification) {
        // Connect to database
        Connection conn = null;
        try {
            conn = databaseController.getConnection();
        } catch ( SQLException e ) {
            e.printStackTrace();
            return;
        }

        // Update notification model
        notificationSettings = notification;


        PreparedStatement ps = null;
        String sql = "UPDATE notifications SET tempNotify='" + notification.getNotifyTemp() + "', tempHigh='" + notification.getTempHigh() + "', tempLow='" + notification.getTempLow() + "', soilNotify='" + notification.getNotifySoil() + "', soilHigh='" + notification.getSoilHigh() + "', soilLow='" + notification.getSoilLow() + "' WHERE id='1'";

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
