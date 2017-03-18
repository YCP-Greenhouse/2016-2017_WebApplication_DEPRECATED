package listener;

import controller.DatabaseController;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

// Get call weather API every hour and save results to database
public class Weather implements ServletContextListener {
    DatabaseController dbController = new DatabaseController();

    public void contextInitialized(ServletContextEvent e) {
        try {
            startTimer();
        } catch( IOException i ) {
            i.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent e) {
        // implementation code
    }

    // Get weather every hour and save it to database
    public void startTimer() throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=York,us&appid=f581e69b118ec3559415b4939e550e33");
        Timer timer = new Timer();

        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    URLConnection urlc = url.openConnection();

                    // Get weather data
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
                    String l = null;
                    String response = "";
                    while ((l=br.readLine())!=null) {
                        response+=l;
                    }
                    br.close();

                    // Parse JSON response
                   JSONObject obj = new JSONObject(response);
                   double temperature = Double.parseDouble(obj.getJSONObject("main").get("temp").toString());
                   double high = Double.parseDouble(obj.getJSONObject("main").get("temp_max").toString());
                   double low = Double.parseDouble(obj.getJSONObject("main").get("temp_min").toString());

                   // Save to database
                    // Connect to database
                    Connection conn = null;
                    try {
                        conn = dbController.getConnection();
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                        return;
                    }

                    PreparedStatement ps = null;
                    String sql = "INSERT INTO weather VALUES(?,?,?,?,?)";


                    conn.setAutoCommit(false);
                    ps = conn.prepareStatement(sql);

                    // Get current time
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date utcTime = new Date();
                    Date date = new Date(utcTime.getTime() + TimeZone.getTimeZone("EST").getRawOffset() );

                    ps.setString(1, null );
                    ps.setDouble(2, KelvinToFahrenheit(temperature) );
                    ps.setDouble(3, KelvinToFahrenheit(high) );
                    ps.setDouble(4, KelvinToFahrenheit(low) );
                    ps.setString( 5, dateFormat.format(date) );

                    ps.executeUpdate();
                    ps.close();

                    conn.commit();
                    conn.close();

                } catch( IOException e ) {
                    e.printStackTrace();
                } catch( JSONException e ) {
                    e.printStackTrace();
                } catch( SQLException e ) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule( hourlyTask, 5000, 1000 * 60 * 60 );
    }

    private double KelvinToFahrenheit( double temp ) {
        return temp*(9.0)/5 - 459.67;
    }

}
