package api;

import controller.AccountController;
import controller.DatabaseController;
import controller.ErrorController;
import model.ErrorModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorAPI extends HttpServlet {

    ErrorController errorController = new ErrorController();
    AccountController accountController = new AccountController();
    DatabaseController databaseController = new DatabaseController();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println( errorController.getLastError() );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

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

            ErrorModel errorModel = new ErrorModel();

            errorModel.setMessage(req.getParameter("message"));
            errorModel.setCode(Integer.parseInt(req.getParameter("code")));
            errorModel.setTime(databaseController.getCurrentTime());

            errorController.updateError(errorModel);

            resp.getWriter().println(errorController.getLastError());
        } else {
            resp.getWriter().println( "Invalid credentials" );
        }

    }


}
