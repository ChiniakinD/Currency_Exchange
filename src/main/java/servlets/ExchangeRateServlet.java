package servlets;

import lombok.extern.slf4j.Slf4j;
import services.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@Slf4j

public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    public ExchangeRateServlet() throws SQLException {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Вызван метод doGet по URI {}", request.getRequestURI());
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            exchangeRateService.getAllExchangeRates(response);
            log.info("Информация по запросу {}", pathInfo);
            log.info("Запрос обработан");
        } else {
            String currencyCode = pathInfo.replace("/", "").toUpperCase();
            exchangeRateService.getExchangeRateByCode(response,currencyCode);
            log.info("Запрос обработан");
        }
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
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("update")) {
            exchangeRateService.updateExchangeRate(request, response, request.getParameter("base"),
                    request.getParameter("target"), new BigDecimal(request.getParameter("rate")));
        }
    }


}
