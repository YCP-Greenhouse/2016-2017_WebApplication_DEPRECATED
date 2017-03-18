package api;

import controller.WeatherController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WeatherAPI extends HttpServlet {
    WeatherController weatherController = new WeatherController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( weatherController.getWeatherData() );

    }
}
