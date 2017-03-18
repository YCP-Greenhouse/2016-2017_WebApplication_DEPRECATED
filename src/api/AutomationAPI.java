package api;


import controller.AccountController;
import controller.AutomationController;
import model.AutomationModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutomationAPI extends HttpServlet {

    AutomationController automationController = new AutomationController();
    AccountController accountController = new AccountController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( automationController.getAutomationSettings() );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

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
            // If null, do nothing
        }

        //String APIkey = "44ffe28b-f470-4bc0-8ee9-38fce01438ce";

        if( user.equals("admin") || accountController.verifyAPIKey(APIkey) ) {

            AutomationModel automationModel = new AutomationModel();

            //automationModel.setHumidity(Integer.parseInt(req.getParameter("humidity")));
            automationModel.setLightIntesity(Integer.parseInt(req.getParameter("light")));
            automationModel.setSoilMoisture(Integer.parseInt(req.getParameter("moisture")));
            automationModel.setTemperature(Integer.parseInt(req.getParameter("temperature")));

            automationController.updateAutomationSettings(automationModel);

        } else {
            resp.getWriter().println("Invalid credentials");
        }
    }
}
