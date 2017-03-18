package api;

import controller.SensorController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AveragesAPI extends HttpServlet{
    SensorController sensorController = new SensorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        String time = "";

        // If time is specified, set time
        if( contains( req.getQueryString(), "time") ) {
            time = req.getParameter("time");
            if( time.equals("a") ) {
                resp.getWriter().println( sensorController.getAllAverages() );
            }
        } else {
            resp.getWriter().println( sensorController.getCurrentAverages() );
        }
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


}
