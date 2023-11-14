package dao;

import java.util.Currency;
import java.util.List;

public interface CurrencyDao {
    public List<Currency> getAllCurrency();
    public Currency getCurrencyById(int id);
    public void createNewCurrency();
}
