package api;

import controller.AccountController;
import controller.ContactController;
import controller.DatabaseController;
import model.ContactModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactAPI extends HttpServlet {

    ContactController contactController = new ContactController();
    AccountController accountController = new AccountController();
    DatabaseController databaseController = new DatabaseController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        resp.getWriter().println(contactController.getAllContactsJSON());
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
            APIkey = "";
        }



        if( user.equals("admin") || accountController.verifyAPIKey(APIkey) ) {

            String action = req.getParameter("action");

            if( action.equals("delete") ) {
                int id = Integer.parseInt(req.getParameter("id"));
                contactController.deleteContact(id);

            } else {
                String name = req.getParameter("name");
                String position = req.getParameter("position");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");


                // Check for invalid input
                if (!databaseController.isValidInput(name)) {
                    return;
                } else if (!databaseController.isValidInput(position)) {
                    return;
                } else if (!databaseController.isValidInput(email)) {
                    return;
                } else if (!databaseController.isValidInput(phone)) {
                    return;
                }

                // Assign values to Contact object
                ContactModel contact = new ContactModel();


                contact.setUsername(name);
                contact.setPosition(position);
                contact.setEmail(email);
                contact.setPhoneNumber(phone);

                if (action.equals("add")) {
                    contactController.addContact(contact);
                } else if (action.equals("update")) {
                    int id = Integer.parseInt(req.getParameter("id"));
                    contact.setId(id);
                    contactController.updateContact(contact);
                }
            }

        } else {
            resp.getWriter().println("Invalid credentials");
        }
    }
}
