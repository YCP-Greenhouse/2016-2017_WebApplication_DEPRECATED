package api;


import controller.AccountController;
import controller.AutomationController;
import model.AutomationModel;
import model.ScheduleModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            AutomationModel currentModel = automationController.getCurrentAutomationSettings();

            HashMap<Integer, ScheduleModel> lightSchedule = new HashMap<>();
            HashMap<Integer, ScheduleModel> waterSchedule = new HashMap<>();

            try {
                int light = Integer.parseInt(req.getParameter("light"));
                automationModel.setLightIntesity(light);
            } catch (NumberFormatException e) {
                automationModel.setLightIntesity(currentModel.getLightIntesity());
            }

            try {
                int humidity = Integer.parseInt(req.getParameter("humidity"));
                automationModel.setHumidity(humidity);
            } catch (NumberFormatException e) {
                automationModel.setHumidity(currentModel.getHumidity());
            }

            try {
                int moisture = Integer.parseInt(req.getParameter("moisture"));
                automationModel.setSoilMoisture(moisture);
            } catch (NumberFormatException e) {
                automationModel.setSoilMoisture(currentModel.getSoilMoisture());
            }

            try {
                int templow = Integer.parseInt(req.getParameter("templow"));
                automationModel.setTempLow(templow);
            } catch (NumberFormatException e) {
                automationModel.setTempLow(currentModel.getTempLow());
            }

            try {
                int temphigh = Integer.parseInt(req.getParameter("temphigh"));
                automationModel.setTempHigh(temphigh);
            } catch (NumberFormatException e) {
                automationModel.setTempHigh(currentModel.getTempHigh());
            }

            // Create enum of parameterName
            Enumeration<String> parameterNames = req.getParameterNames();

            // Iterate through params to find schedule values
            while( parameterNames.hasMoreElements() ) {

                String param = parameterNames.nextElement();

                if( !isValidInput(param) ) {
                    resp.getWriter().println("Invalid input");
                    break;
                }

                // Get water schedule entries
                if( contains( param, "waterschedule") ) {

                    // If key doesn't exist, add it
                    if( !waterSchedule.containsKey(Integer.parseInt(getDataNumber(param))) ) {
                        ScheduleModel newSchedule = new ScheduleModel();
                        newSchedule.setId(Integer.parseInt(getDataNumber(param)));
                        waterSchedule.put( Integer.parseInt(getDataNumber(param)), newSchedule);
                    }

                    if( contains( param, "zone") ) {
                        ScheduleModel schedule = waterSchedule.get(Integer.parseInt(getDataNumber(param)));

                        schedule.setZoneID( Integer.parseInt( req.getParameter(param) ) );

                        waterSchedule.remove(getDataNumber(param));
                        waterSchedule.put(Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "starttime") ) {
                        ScheduleModel schedule = waterSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setStartTime( req.getParameter(param) );

                        waterSchedule.remove(getDataNumber(param));
                        waterSchedule.put(Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "endtime") ) {
                        ScheduleModel schedule = waterSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setEndTime( req.getParameter(param) );

                        waterSchedule.remove(getDataNumber(param));
                        waterSchedule.put(Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "hours") ) {
                        ScheduleModel schedule = waterSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setHours( Double.parseDouble(req.getParameter(param)) );

                        waterSchedule.remove(getDataNumber(param));
                        waterSchedule.put(Integer.parseInt(getDataNumber(param)), schedule);
                    }

                    // Get light schedule entries
                } else if( contains( param, "lightschedule") ) {

                    // If key doesn't exist, add it
                    if( !lightSchedule.containsKey(Integer.parseInt(getDataNumber(param))) ) {
                        ScheduleModel newSchedule = new ScheduleModel();
                        newSchedule.setId(Integer.parseInt(getDataNumber(param)));
                        lightSchedule.put( Integer.parseInt(getDataNumber(param)), newSchedule);
                    }

                    if( contains( param, "zone") ) {
                        ScheduleModel schedule = lightSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setZoneID( Integer.parseInt(req.getParameter(param)) );

                        lightSchedule.remove(getDataNumber(param));
                        lightSchedule.put( Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "starttime") ) {
                        ScheduleModel schedule = lightSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setStartTime( req.getParameter(param) );

                        lightSchedule.remove(getDataNumber(param));
                        lightSchedule.put( Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "endtime") ) {
                        ScheduleModel schedule = lightSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setEndTime( req.getParameter(param) );

                        lightSchedule.remove(getDataNumber(param));
                        lightSchedule.put( Integer.parseInt(getDataNumber(param)), schedule);

                    } else if( contains( param, "hours") ) {
                        ScheduleModel schedule = lightSchedule.get(Integer.parseInt(getDataNumber(param)));
                        schedule.setHours( Double.parseDouble(req.getParameter(param)) );

                        lightSchedule.remove(getDataNumber(param));
                        lightSchedule.put( Integer.parseInt(getDataNumber(param)), schedule);
                    }
                }
            }

            // Add schedules to automation model
            for( int i=0; i<lightSchedule.size(); i++ ) {
                automationModel.addLightSchedule(lightSchedule.get(i));
            }


            for( int i=0; i<waterSchedule.size(); i++ ) {
                automationModel.addWaterSchedule(waterSchedule.get(i));
            }

            //System.out.println("AutomationAPI\nTemp low: " + automationModel.getTempLow() + "\nTemp high: " + automationModel.getTempHigh() + "\nMoisture: " + automationModel.getSoilMoisture() + "\nHumidity: " + automationModel.getHumidity() + "\nLight: " + automationModel.getLightIntesity());

            automationController.updateAutomationSettings(automationModel);

        } else {
            resp.getWriter().println("Invalid credentials");
        }
    }

    public String getDataNumber( String param ) {

        param = param.replaceAll("\\D+", "");

        try{
            return param;
        } catch( NullPointerException e ) {
            return "0";
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

    // Checks input for invalid String characters
    public boolean isValidInput(String input) {
        int count = 0;
        count = input.replaceAll("[^\";'`()~]", "").length();

        if( count == 0 ) {
            return true;
        } else {
            return false;
        }
    }
}


