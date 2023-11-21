package servlets;

import services.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    public ExchangeRateServlet() throws SQLException {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("addNew")) {
            try {
                exchangeRateService.addNewExchangeRate(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getAll".equals(action)) {
            exchangeRateService.getAllExchangeRates(request, response);
        } else if ("getByCode".equals(action)) {
            exchangeRateService.getExchangeRateByCode(request, response, request.getParameter("base"), request.getParameter("target"));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

}
