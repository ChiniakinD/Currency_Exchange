package entities;

import java.math.BigDecimal;

public class Exchange extends ExchangeRate{
    private int amount;
    private BigDecimal sum;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
