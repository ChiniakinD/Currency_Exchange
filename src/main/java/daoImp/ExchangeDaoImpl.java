package daoImp;

import entities.Exchange;
import entities.ExchangeRate;
import entities.ExchangeRateDto;

import java.math.RoundingMode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeDaoImpl {
    private Connection connection;

    public ExchangeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, int amount) {
        Exchange exchange = new Exchange();
        if (isExchangeRateExist(getCurrencyIdByCode(baseCurrencyCode), getCurrencyIdByCode(targetCurrencyCode))) {
            exchange = straightExchange(baseCurrencyCode, targetCurrencyCode, amount);
        } else if (isExchangeRateExist(getCurrencyIdByCode(targetCurrencyCode), getCurrencyIdByCode(baseCurrencyCode))) {
            exchange = reverseExchange(baseCurrencyCode, targetCurrencyCode, amount);
        } else if (isExchangeRateExist(getCurrencyIdByCode("USD"), getCurrencyIdByCode(baseCurrencyCode)) &&
                isExchangeRateExist(getCurrencyIdByCode("USD"), getCurrencyIdByCode(targetCurrencyCode))) {
            exchange = crossExchangeByUSD(baseCurrencyCode, targetCurrencyCode, amount);
        }
        exchange.setSum(BigDecimal.valueOf(amount).multiply(exchange.getRate()));
        return exchange;
    }

    private Exchange straightExchange(String baseCurrencyCode, String targetCurrencyCode, int amount) {
        Exchange exchange = new Exchange();
        ExchangeRateDto exchangeRateDto = new ExchangeRateDaoImpl(connection).getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
        exchange.setId(exchange.getId());
        exchange.setBaseCurrency(exchangeRateDto.getBaseCurrency());
        exchange.setTargetCurrency(exchangeRateDto.getTargetCurrency());
        exchange.setRate(exchangeRateDto.getRate());
        exchange.setAmount(amount);
        return exchange;
    }

    private Exchange reverseExchange(String baseCurrencyCode, String targetCurrencyCode, int amount) {
        Exchange exchange = new Exchange();
        ExchangeRateDto exchangeRateDto = new ExchangeRateDaoImpl(connection).getExchangeRateByCode(targetCurrencyCode, baseCurrencyCode);
        exchange.setId(exchange.getId());
        exchange.setBaseCurrency(exchangeRateDto.getTargetCurrency());
        exchange.setTargetCurrency(exchangeRateDto.getBaseCurrency());
        exchange.setRate(BigDecimal.ONE.divide(exchangeRateDto.getRate(), MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_UP));
        exchange.setAmount(amount);
        return exchange;
    }

    private Exchange crossExchangeByUSD(String baseCurrencyCode, String targetCurrencyCode, int amount) {
        Exchange exchange = new Exchange();
        if (isExchangeRateExist(getCurrencyIdByCode("USD"), getCurrencyIdByCode(baseCurrencyCode)) &&
                isExchangeRateExist(getCurrencyIdByCode("USD"), getCurrencyIdByCode(targetCurrencyCode))) {
            ExchangeRate baseExchangeRate = straightExchange("USD", baseCurrencyCode, amount);
            ExchangeRate targetExchangeRate = straightExchange("USD", targetCurrencyCode, amount);
            exchange.setBaseCurrency(baseExchangeRate.getTargetCurrency());
            exchange.setTargetCurrency(targetExchangeRate.getTargetCurrency());
            exchange.setRate(baseExchangeRate.getRate().divide(targetExchangeRate.getRate(), 2, RoundingMode.HALF_UP));
            exchange.setAmount(amount);
        }
        return exchange;
    }

    private int getCurrencyIdByCode(String code) {
        return new CurrencyDaoImpl(connection).getCurrencyByCode(code).getId();
    }

    private boolean isExchangeRateExist(int BaseCurrencyId, int TargetCurrencyId) {
        String sql = "Select * From Exchange_Rate Where BaseCurrencyId = ? and TargetCurrencyId = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, BaseCurrencyId);
            preparedStatement.setInt(2, TargetCurrencyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
