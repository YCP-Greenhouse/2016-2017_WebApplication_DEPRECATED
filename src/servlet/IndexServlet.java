package servlet;

import controller.StateController;
import model.StateModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    StateController stateController = new StateController();
    StateModel stateModel = new StateModel();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException {

        stateModel = stateController.getStoredState();
        stateController.setCurrentState(stateModel);

        req.getRequestDispatcher("/views/apitest.jsp").forward(req, resp);
    }
}
