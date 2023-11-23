package converters;

import dto.CurrencyValueDto;
import entities.CurrencyValue;

public class CurrencyValueConverter {
    public static CurrencyValue dtoToEntity(CurrencyValueDto currencyValueDto) {
        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setId(currencyValueDto.getId());
        currencyValue.setCode(currencyValueDto.getCode());
        currencyValue.setFullName(currencyValueDto.getFullName());
        currencyValue.setSign(currencyValueDto.getSign());
        return currencyValue;
    }

    public static CurrencyValueDto entityToDto(CurrencyValue currencyValue) {
        CurrencyValueDto currencyValueDto = new CurrencyValueDto();
        currencyValueDto.setId(currencyValue.getId());
        currencyValueDto.setCode(currencyValue.getCode());
        currencyValueDto.setFullName(currencyValue.getFullName());
        currencyValueDto.setSign(currencyValue.getSign());
        return currencyValueDto;
    }
}
