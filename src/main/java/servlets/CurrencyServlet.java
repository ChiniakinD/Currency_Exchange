package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import daoImp.CurrencyDaoImp;
import db.DataBaseConnection;
import entities.CurrencyValue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Currency;
import java.util.List;


public class CurrencyServlet extends HttpServlet {
    private CurrencyDao currencyDao;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DataBaseConnection.getConnection();
            currencyDao = new CurrencyDaoImp(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("addNew".equals(action)) {
            addNewCurrency(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getAll".equals(action)) {
            // Обработка запроса по умолчанию
            getAllCurrencies(request, response);
        } else if ("getById".equals(action)) {
            int currencyId = Integer.parseInt(request.getParameter("id"));
            getCurrencyById(request, response, currencyId);
        } else if ("getByCode".equals(action)) {
            String currencyCode = request.getParameter("code");
            getCurrencyByCode(request, response, currencyCode);
        } else if ("addNew".equals(action)) {
            addNewCurrency(request, response);
        } else {
            // Обработка неизвестного запроса
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void getCurrencyById(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        CurrencyValue currencyValue = currencyDao.getCurrencyById(id);
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencyValue);
        } catch (JsonProcessingException e) {
            System.out.println("Poka tyt error");
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }

    private void getCurrencyByCode(HttpServletRequest request, HttpServletResponse response, String code)
            throws ServletException, IOException {
        CurrencyValue currencyValue = currencyDao.getCurrencyByCode(code);
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencyValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }

    private void addNewCurrency(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setCode(request.getParameter("code"));
        currencyValue.setFullName(request.getParameter("fullName"));
        currencyValue.setSign(request.getParameter("sign"));
        currencyDao.addNewCurrency(currencyValue);
    }


    private void getAllCurrencies(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("начало get запроса");
        List<CurrencyValue> currencies = currencyDao.getAllCurrency();
        System.out.println("currencies = " + currencies);
        String currenciesJson = null;
        System.out.println("Poka tyt 1");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencies);
            System.out.println("Poka tyt 2");
        } catch (JsonProcessingException e) {
            System.out.println("Poka tyt error");
            throw new RuntimeException(e);
        }
        //String json = objectMapper.writeValueAsString(currencies);
        System.out.println("map currencies " + currenciesJson);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }

}
