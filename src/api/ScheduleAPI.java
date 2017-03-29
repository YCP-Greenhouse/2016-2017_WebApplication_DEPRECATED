package api;

import controller.AccountController;
import controller.ScheduleController;
import model.ScheduleModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScheduleAPI extends HttpServlet {

    ScheduleController scheduleController = new ScheduleController();
    AccountController accountController = new AccountController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( scheduleController.getSchedules() );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ScheduleModel scheduleModel = new ScheduleModel();

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
            String start = req.getParameter("start");
            String end = req.getParameter("end");
            String type = req.getParameter("type");
            double hours = Double.parseDouble(req.getParameter("hours"));
            int inverse = Integer.parseInt(req.getParameter("inverse"));
            int zoneID = Integer.parseInt(req.getParameter("zoneid"));
            int id = 0;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch( NullPointerException e ) {
                id = -1;
            } catch( NumberFormatException e ) {
                id = -1;
            }
            String date = req.getParameter("date");

            // Set ScheduleModel
            scheduleModel.setId(id);
            scheduleModel.setZoneID(zoneID);
            scheduleModel.setStartTime(date + " " + convertTime(start)+":00:00");
            scheduleModel.setEndTime(date + " " + convertTime(end)+":00:00");
            scheduleModel.setHours(hours);
            scheduleModel.setInverse(inverse);

            //System.out.println("Start: " + start + "\nEnd: " + end + "\nHours: " + hours + "\nInverse: " + inverse + "\nid: " + id + "\nDate: " + date );

            // If ID = -1, it doesn't exist so add it
            if( id == -1 ) {
                scheduleController.addSchedule(type, scheduleModel);
            } else {
                scheduleController.updateSchedule(type, scheduleModel);
            }
        } else {
            resp.getWriter().println("Invalid Credentials");
        }
    }

    public String convertTime(String time) {

        String ampm = time.substring(time.length()-2, time.length());
        time = time.substring(0,time.length()-2);

        int t = Integer.parseInt(time);
        if( ampm.equals("pm") ) {
            t += 12;
        }

        return Integer.toString(t);
    }

}
