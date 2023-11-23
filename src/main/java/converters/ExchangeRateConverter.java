package converters;

import dto.CurrencyValueDto;
import entities.CurrencyValue;
import entities.ExchangeRate;
import entities.ExchangeRateDto;

public class ExchangeRateConverter {
    public static ExchangeRate dtoToEntity(ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(exchangeRateDto.getId());
        exchangeRate.setBaseCurrency(exchangeRateDto.getBaseCurrency());
        exchangeRate.setTargetCurrency(exchangeRateDto.getTargetCurrency());
        exchangeRate.setRate(exchangeRateDto.getRate());
        return exchangeRate;
    }

    public static ExchangeRateDto entityToDto(ExchangeRate exchangeRate) {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setId(exchangeRate.getId());
        exchangeRateDto.setBaseCurrency(exchangeRate.getBaseCurrency());
        exchangeRateDto.setTargetCurrency(exchangeRate.getTargetCurrency());
        exchangeRateDto.setRate(exchangeRate.getRate());
        return exchangeRateDto;
    }
}
