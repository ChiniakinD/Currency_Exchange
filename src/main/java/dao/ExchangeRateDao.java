package dao;

import entities.CurrencyValue;
import entities.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateDao {
    List<ExchangeRate> getAllExchangeRates();

    ExchangeRate getExchangeRateByCode(String code, String code1);

    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    void addNewExchangeRate(ExchangeRate exchangeRate);

}
