package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import daoImp.CurrencyDaoImpl;
import db.DataBaseConnection;
import dto.CurrencyValueDto;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@Slf4j

public class CurrencyService {
    private CurrencyDaoImpl currencyDaoImpl;

    public CurrencyService() throws SQLException {
        this.currencyDaoImpl = new CurrencyDaoImpl(DataBaseConnection.getConnection());
    }

    public void getAllCurrencies(HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Начало get запроса getAllCurrencies");
        List<CurrencyValueDto> currencies = currencyDaoImpl.getAllCurrency();
        log.info("Currencies = ", currencies);
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencies);
            log.info("Запрос переведен в строку {}" , currenciesJson);
        } catch (JsonProcessingException e) {
            log.error("Error {}",e);
            throw new RuntimeException(e);
        }
        log.info("Map currencies " + currenciesJson);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }
    public void getCurrencyById(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        log.info("Начало get запроса getCurrencyById");
        CurrencyValueDto currencyValueDto = currencyDaoImpl.getCurrencyById(id);
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencyValueDto);
            log.info("Запрос переведен в строку {}" , currenciesJson);
        } catch (JsonProcessingException e) {
            log.error("Error {}",e);
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }
    public void getCurrencyByCode(HttpServletResponse response, String code)
            throws ServletException, IOException {
        log.info("Начало get запроса getCurrencyByCode");
        CurrencyValueDto currencyValueDto = currencyDaoImpl.getCurrencyByCode(code);
        String currenciesJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            currenciesJson = objectMapper.writeValueAsString(currencyValueDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(currenciesJson);
    }

    public void addNewCurrency(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Начало Post запроса addNewCurrency");
        CurrencyValueDto currencyValueDto = new CurrencyValueDto();
        currencyValueDto.setCode(request.getParameter("code"));
        currencyValueDto.setFullName(request.getParameter("fullName"));
        currencyValueDto.setSign(request.getParameter("sign"));
        currencyDaoImpl.addNewCurrency(currencyValueDto);
        log.info("Валюта {} добавлена", currencyValueDto);
    }
}
