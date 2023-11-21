package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import daoImp.CurrencyDaoImpl;
import db.DataBaseConnection;
import entities.CurrencyValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CurrencyService {
    private CurrencyDaoImpl currencyDaoImpl;

    public CurrencyService() throws SQLException {
        this.currencyDaoImpl = new CurrencyDaoImpl(DataBaseConnection.getConnection());
    }

    public void getAllCurrencies(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("начало get запроса");
        List<CurrencyValue> currencies = currencyDaoImpl.getAllCurrency();
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
        System.out.println("map currencies " + currenciesJson);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }
    public void getCurrencyById(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        CurrencyValue currencyValue = currencyDaoImpl.getCurrencyById(id);
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
    public void getCurrencyByCode(HttpServletRequest request, HttpServletResponse response, String code)
            throws ServletException, IOException {
        CurrencyValue currencyValue = currencyDaoImpl.getCurrencyByCode(code);
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

    public void addNewCurrency(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setCode(request.getParameter("code"));
        currencyValue.setFullName(request.getParameter("fullName"));
        currencyValue.setSign(request.getParameter("sign"));
        currencyDaoImpl.addNewCurrency(currencyValue);
    }
}
