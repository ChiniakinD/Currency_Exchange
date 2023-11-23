package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import converters.CurrencyValueConverter;
import daoImp.CurrencyDaoImpl;
import daoImp.ExchangeRateDaoImpl;
import db.DataBaseConnection;
import entities.ExchangeRate;
import entities.ExchangeRateDto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {
    private ExchangeRateDaoImpl exchangeRateDaoImpl;
    public ExchangeRateService() throws SQLException {
        this.exchangeRateDaoImpl = new ExchangeRateDaoImpl(DataBaseConnection.getConnection());
    }

    public void getExchangeRateByCode(HttpServletRequest request, HttpServletResponse response,
                                       String baseCurrencyCode, String targetCurrencyCode)
            throws ServletException, IOException {
        ExchangeRateDto exchangeRateDto = exchangeRateDaoImpl.getExchangeRateByCode(baseCurrencyCode,targetCurrencyCode);
        String currencyJson = null;
        try{
            ObjectMapper objectMapper= new ObjectMapper();
            currencyJson = objectMapper.writeValueAsString(exchangeRateDto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currencyJson);
    }

    public void getAllExchangeRates(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ExchangeRateDto> exchangeRates = exchangeRateDaoImpl.getAllExchangeRates();
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
    public void addNewExchangeRate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Connection connection = DataBaseConnection.getConnection();
        CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl(connection);
        ExchangeRate exchangeRate =new ExchangeRate();
        exchangeRate.setBaseCurrency(CurrencyValueConverter.dtoToEntity(currencyDaoImpl.getCurrencyByCode((request.getParameter("baseCurrency")))));
        exchangeRate.setTargetCurrency(CurrencyValueConverter.dtoToEntity(currencyDaoImpl.getCurrencyByCode((request.getParameter("targetCurrency")))));
        exchangeRate.setRate(new BigDecimal(request.getParameter("rate")));
        exchangeRateDaoImpl.addNewExchangeRate(exchangeRate);
    }
    public void updateExchangeRate(HttpServletRequest request, HttpServletResponse response,
                                   String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate)
    throws ServletException, IOException{
        ExchangeRate exchangeRate = exchangeRateDaoImpl.updateExchangeRate(baseCurrencyCode,targetCurrencyCode,rate);
        ObjectMapper objectMapper = new ObjectMapper();
        String currenciesJson = null;
        try {
            currenciesJson = objectMapper.writeValueAsString(exchangeRate);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(currenciesJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
