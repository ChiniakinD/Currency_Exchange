package servlets;
import services.CurrencyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = new CurrencyService();

    public CurrencyServlet() throws SQLException {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("addNew".equals(action)) {
            currencyService.addNewCurrency(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getAll".equals(action)) {
                currencyService.getAllCurrencies(request,response);
        } else if ("getById".equals(action)) {
            int currencyId = Integer.parseInt(request.getParameter("id"));
            currencyService.getCurrencyById(request, response, currencyId);
        } else if ("getByCode".equals(action)) {
            String currencyCode = request.getParameter("code");
            currencyService.getCurrencyByCode(request, response, currencyCode);
        } else if ("addNew".equals(action)) {
            currencyService.addNewCurrency(request, response);
        } else {
            // Обработка неизвестного запроса
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

}
