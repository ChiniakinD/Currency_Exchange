package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import dao.ExchangeRateDao;
import daoImp.CurrencyDaoImpl;
import daoImp.ExchangeRateDaoImpl;
import db.DataBaseConnection;
import entities.CurrencyValue;
import entities.ExchangeRate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateDao exchangeRateDao;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DataBaseConnection.getConnection();
            exchangeRateDao = new ExchangeRateDaoImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String action = request.getParameter("action");
        if (action.equals("addNew")) {
            try {
                addNewExchangeRate(request,response);
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
            getAllExchangeRates(request, response);
        } else if ("getByCode".equals(action)) {
            getExchangeRateByCode(request,response,request.getParameter("base"),request.getParameter("target"));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void getExchangeRateByCode(HttpServletRequest request, HttpServletResponse response,
                                       String baseCurrencyCode, String targetCurrencyCode)
            throws ServletException, IOException {
        ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCurrencyCode,targetCurrencyCode);
        String currencyJson = null;
        try{
            ObjectMapper objectMapper= new ObjectMapper();
            currencyJson = objectMapper.writeValueAsString(exchangeRate);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currencyJson);
    }

    private void getAllExchangeRates(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAllExchangeRates();
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(exchangeRates);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("map currencies " + currenciesJson);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }
    private void addNewExchangeRate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Connection connection = DataBaseConnection.getConnection();
        CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl(connection);
        ExchangeRate exchangeRate =new ExchangeRate();
        exchangeRate.setBaseCurrency(currencyDaoImpl.getCurrencyByCode((request.getParameter("baseCurrency"))));
        exchangeRate.setTargetCurrency(currencyDaoImpl.getCurrencyByCode((request.getParameter("targetCurrency"))));
        exchangeRate.setRate(new BigDecimal(request.getParameter("rate")));
        exchangeRateDao.addNewExchangeRate(exchangeRate);
    }
}
