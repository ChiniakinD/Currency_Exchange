package servlets;

import lombok.extern.slf4j.Slf4j;
import services.CurrencyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = new CurrencyService();

    public CurrencyServlet() throws SQLException {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Вызван метод doPost по URI {}", request.getRequestURI());
        String action = request.getParameter("action");
        if ("addNew".equals(action)) {
            log.info("Вызван запрос для добавления валюты");
            currencyService.addNewCurrency(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Вызван метод doGet по URI {}", request.getRequestURI());
        String action = request.getParameter("action");
        if ("getAll".equals(action)) {
            log.info("Вызван запрос для вывода всех валют");
            currencyService.getAllCurrencies(response);
        } else if ("getById".equals(action)) {
            log.info("Вызван запрос для вывода валюты по id");
            int currencyId = Integer.parseInt(request.getParameter("id"));
            currencyService.getCurrencyById(request, response, currencyId);
        } else if ("getByCode".equals(action)) {
            log.info("Вызван запрос для вывода валюты по code");
            String currencyCode = request.getParameter("code");
            currencyService.getCurrencyByCode(response, currencyCode);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }
}
