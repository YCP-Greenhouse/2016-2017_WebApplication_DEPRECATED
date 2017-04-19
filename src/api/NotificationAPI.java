package api;

import controller.NotificationController;
import model.NotificationModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NotificationAPI extends HttpServlet {
    NotificationController notificationController = new NotificationController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( notificationController.getNotificationSettings() );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        // Verify user identity
        String user = "";
        try {
            user = req.getSession().getAttribute("user").toString();

        } catch( NullPointerException e ) {
            // If null, do nothing
        }

        if( user.equals("admin") ) {
            NotificationModel notification = new NotificationModel();

            notification.setNotifyTemp(Integer.parseInt(req.getParameter("tempNotify")));
            notification.setTempHigh(Integer.parseInt(req.getParameter("tempHigh")));
            notification.setTempLow(Integer.parseInt(req.getParameter("tempLow")));
            notification.setNotifySoil(Integer.parseInt(req.getParameter("soilNotify")));
            notification.setSoilHigh(Integer.parseInt(req.getParameter("soilHigh")));
            notification.setSoilLow(Integer.parseInt(req.getParameter("soilLow")));

            notificationController.updateNotifications(notification);
        }

    }

}
