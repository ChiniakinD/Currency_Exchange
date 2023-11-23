package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import daoImp.ExchangeDaoImpl;
import db.DataBaseConnection;
import entities.Exchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ExchangeService {
    private ExchangeDaoImpl exchangeDaoImpl;

    public ExchangeService() throws SQLException {
        this.exchangeDaoImpl = new ExchangeDaoImpl(DataBaseConnection.getConnection());
    }

    public void exchange(HttpServletResponse response,
                         String baseCurrencyCode, String targetCurrencyCode, int amount) throws IOException {
        Exchange exchange = exchangeDaoImpl.exchange(baseCurrencyCode,targetCurrencyCode,amount);
        String currencyJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currencyJson = objectMapper.writeValueAsString(exchange);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset = UTF-8");
        response.getWriter().write(currencyJson);
    }
}
