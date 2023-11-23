package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import converters.CurrencyValueConverter;
import daoImp.CurrencyDaoImpl;
import daoImp.ExchangeRateDaoImpl;
import db.DataBaseConnection;
import entities.ExchangeRate;
import entities.ExchangeRateDto;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class ExchangeRateService {
    private ExchangeRateDaoImpl exchangeRateDaoImpl;
    public ExchangeRateService() throws SQLException {
        this.exchangeRateDaoImpl = new ExchangeRateDaoImpl(DataBaseConnection.getConnection());
    }

    public void getExchangeRateByCode(HttpServletResponse response, String exchangeCodes)
            throws ServletException, IOException {
        log.info("Вызван метод получения сущности ExchangeRate по кодам");
        ObjectMapper objectMapper = new ObjectMapper();
        if (!isValid(exchangeCodes)) {
            log.warn("Введен неверный код валют {}", exchangeCodes);
            response.setStatus(400);
            return;
        }
        log.info("Происходит поиск кодов {}, {}", exchangeCodes.substring(0, 3), exchangeCodes.substring(3));
        ExchangeRateDto exchangeRateDto = exchangeRateDaoImpl.getExchangeRateByCode(exchangeCodes.substring(0, 3), exchangeCodes.substring(3));
        if (exchangeRateDto != null) {
            log.info("Полученные данные {}", exchangeRateDto);
            response.setStatus(200);
            response.setContentType("application/json; charset=UTF-8");
            try {
                response.getWriter().write(objectMapper.writeValueAsString(exchangeRateDto));
            } catch (IOException e) {
                log.error("Ошибка при отправке данных в ответ", e);
                throw new RuntimeException(e);
            }
        } else if (exchangeRateDto == null) {
            log.warn("Не найден курс валют по общему коду в базе данных {}", exchangeCodes);
            response.setStatus(404);
        }
    }


    public void getAllExchangeRates(HttpServletResponse response)
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
    private boolean isValid(String input) {
        String regex = "^[a-zA-Z]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
