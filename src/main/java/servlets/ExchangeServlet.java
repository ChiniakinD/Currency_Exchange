package servlets;

import services.ExchangeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService;

    public ExchangeServlet() throws SQLException {
        this.exchangeService = new ExchangeService();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("exchange".equals(action)) {
            exchangeService.exchange(response, request.getParameter("from"),
                    request.getParameter("to"), Integer.parseInt(request.getParameter("amount")));
        }
    }
}
