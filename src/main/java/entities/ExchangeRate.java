package entities;

import java.math.BigDecimal;

public class ExchangeRate {
    private int id;
    private CurrencyValue baseCurrency;
    private CurrencyValue targetCurrency;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public CurrencyValue getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyValue baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyValue getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyValue targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}
