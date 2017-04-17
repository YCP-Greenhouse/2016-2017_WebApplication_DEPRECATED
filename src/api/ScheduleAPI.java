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

            int id;

            // Get id, type and action. These parameters are universal for post/delete requests

            // Try to get id. Empty schedule cells will not return ids
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch (NullPointerException e) {
                id = -1;
            } catch (NumberFormatException e) {
                id = -1;
            }

            // Get schedule
            String schedule = req.getParameter("schedule");

            // Get type
            String type = req.getParameter("type");



            // Try to get action. Only delete posts have the 'action' parameter
            String action = "";
            try {
                action = req.getParameter("action");
            } catch ( NullPointerException e ) {
                action = "post";
            }

            if( action == null ) {
                action = "post";
            }

            // If action doesn't equal delete, handle post normally
            if( !action.equals("delete") ) {

                String start = req.getParameter("start");
                String end = req.getParameter("end");

                double hours = Double.parseDouble(req.getParameter("hours"));
                int day = Integer.parseInt(req.getParameter("date"));
                int inverse = Integer.parseInt(req.getParameter("inverse"));
                int zoneID = Integer.parseInt(req.getParameter("zoneid"));

                int threshold = -1;

                // If type is 'sensor' get threshold
                if( type.equals("sensors")) {
                    threshold = Integer.parseInt(req.getParameter("threshold"));
                }

                // Set ScheduleModel
                scheduleModel.setId(id);
                scheduleModel.setZoneID(zoneID);
                scheduleModel.setDay(day);
                scheduleModel.setStartTime(start);
                scheduleModel.setEndTime(end);
                scheduleModel.setHours(hours);
                scheduleModel.setInverse(inverse);
                scheduleModel.setType(type);
                scheduleModel.setThreshold(threshold);

                // If ID = -1, it doesn't exist so add it
                if (id == -1) {
                    scheduleController.addSchedule(schedule, scheduleModel);
                } else {
                    scheduleController.updateSchedule(schedule, scheduleModel);
                }
            } else {
                System.out.println("Delete id: " + id );
                scheduleController.deleteSchedule(id, schedule);
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
        } else if( t == 12 ) {
            t = 0;
        }

        System.out.println("Converting " + time + " to " + t );

        return Integer.toString(t);
    }

}
