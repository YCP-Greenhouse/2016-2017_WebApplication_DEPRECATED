package servlet;

import controller.AccountController;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;



public class LoginServlet extends HttpServlet {

    AccountController accountController = new AccountController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/settings.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        String user = req.getParameter("username");
        String password = req.getParameter("password");

        if( accountController.verifyUser( user, password ) ) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user );

            // Set session to expire after 60 minutes
            session.setMaxInactiveInterval(3600);
            Cookie username = new Cookie("user", accountController.generateHash(user) );
            username.setMaxAge(3600);

            resp.addCookie(username);

        } else {
            JSONObject obj = new JSONObject();

            try {
                obj.put("message", "Invalid login credentials");
            } catch( JSONException e ) {
                e.printStackTrace();
            }

            resp.getWriter().println(obj);
        }

        req.getRequestDispatcher("/views/settings.jsp").forward(req, resp);
    }


}
