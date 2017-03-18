package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AccountController;
import controller.StateController;

public class StateAPI extends HttpServlet {
    private static final long serialVersionUID = 1L;

    StateController stateController = new StateController();
    AccountController accountController = new AccountController();

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( stateController.getCurrentStateJSON() );

    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp )
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

        if( user.equals("admin") || accountController.verifyAPIKey(APIkey) ) {

            String lights = req.getParameter("lights");
            String heater = req.getParameter("heater");
            String pump = req.getParameter("pump");
            String fans = req.getParameter("fans");
            String vents = req.getParameter("vents");
            String shades = req.getParameter("shades");

            stateController.setCurrentState(convertToBoolean(req.getParameter("lights")), convertToBoolean(req.getParameter("heater")), convertToBoolean(req.getParameter("pump")), convertToBoolean(req.getParameter("fans")), convertToBoolean(req.getParameter("vents")), convertToBoolean(req.getParameter("shades")));
            stateController.storeCurrentState();

            resp.getWriter().println(stateController.getCurrentStateJSON());
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
