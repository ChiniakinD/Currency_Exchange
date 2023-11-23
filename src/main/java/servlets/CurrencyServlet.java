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
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Вызван метод doGet по URI {}", request.getRequestURI());
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            currencyService.getAllCurrencies(response);
            log.info("Информация по запросу {}", pathInfo);
            log.info("Запрос обработан");
        } else {
            String currencyCode = pathInfo.replace("/", "").toUpperCase();
            currencyService.getCurrencyByCode(response,currencyCode);
            log.info("Информация по запросу {}", pathInfo);
            log.info("Запрос обработан");
        }
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






}
