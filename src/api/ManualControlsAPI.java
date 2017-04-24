package api;

import controller.AccountController;
import controller.ManualControlsController;
import model.ManualControlsModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ManualControlsAPI extends HttpServlet {
    ManualControlsController manualControlsController = new ManualControlsController();
    AccountController accountController = new AccountController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( manualControlsController.getManualControls() );

    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        // Verify user identity
        String user = "";
        String APIkey = "";
        try {
            user = req.getSession().getAttribute("user").toString();
        } catch (NullPointerException e) {
            // If null, do nothing
        }

        try {
            APIkey = req.getParameter("apikey");
        } catch (NullPointerException e) {
            // If null, do nothing
        }

        if (user.equals("admin") || accountController.verifyAPIKey(APIkey)) {
            boolean lights = convertToBoolean(req.getParameter("lights"));
            boolean shades = convertToBoolean(req.getParameter("shades"));
            boolean water = convertToBoolean(req.getParameter("water"));
            boolean fans = convertToBoolean(req.getParameter("fans"));
            boolean lightoverride = convertToBoolean(req.getParameter("lightoverride"));
            boolean wateroverride = convertToBoolean(req.getParameter("wateroverride"));

            ManualControlsModel controls = new ManualControlsModel();

            controls.setLights(lights);
            controls.setShades(shades);
            controls.setFans(fans);
            controls.setWater(water);
            controls.setLightOverride(lightoverride);
            controls.setWaterOverride(wateroverride);

            manualControlsController.updateControls(controls);

            resp.getWriter().println(manualControlsController.getManualControls());

        } else {
            resp.getWriter().println("Invalid credentials");
        }
    }

    private boolean convertToBoolean(String input) {
        if( input == null ) {
            return false;
        }
        if( input.contains("on") || input.contains("true")) {
            return true;
        } else {
            return false;
        }
    }
}
