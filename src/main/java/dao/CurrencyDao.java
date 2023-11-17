package dao;

import entities.CurrencyValue;

import java.util.List;

public interface CurrencyDao {
    List<CurrencyValue> getAllCurrency();
    CurrencyValue getCurrencyById(int id);
    CurrencyValue getCurrencyByCode(String code);
    void addNewCurrency(CurrencyValue currencyValue);
}
