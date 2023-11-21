package daoImp;

import dao.ExchangeRateDao;
import db.DataBaseConnection;
import entities.CurrencyValue;
import entities.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
    private Connection connection;

    public ExchangeRateDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM exchange_rate";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getInt("id"));
                exchangeRate.setBaseCurrency(new CurrencyDaoImpl(connection).getCurrencyById(resultSet.getInt("baseCurrencyId")));
                exchangeRate.setTargetCurrency(new CurrencyDaoImpl(connection).getCurrencyById(resultSet.getInt("targetCurrencyId")));
                exchangeRate.setRate(resultSet.getBigDecimal("Rate"));
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate getExchangeRateByCode(
            String baseCurrencyCode, String targetCurrencyCode) {
        String sql = "SELECT er.ID, er.Rate, " +
                "baseCurrency.ID AS baseCurrencyId, " +
                "targetCurrency.ID AS targetCurrencyId " +
                "FROM exchange_rate er " +
                "JOIN currency baseCurrency ON er.BaseCurrencyId = baseCurrency.ID " +
                "JOIN currency targetCurrency ON er.TargetCurrencyId = targetCurrency.ID " +
                "WHERE baseCurrency.Code = ? AND targetCurrency.Code = ?";
        ExchangeRate exchangeRate = new ExchangeRate();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getInt("Id"));
                exchangeRate.setBaseCurrency(new CurrencyDaoImpl(connection).getCurrencyByCode(baseCurrencyCode));
                exchangeRate.setTargetCurrency(new CurrencyDaoImpl(connection).getCurrencyByCode(targetCurrencyCode));
                exchangeRate.setRate(resultSet.getBigDecimal("rate"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String sql = "Update exchange_rate Set Rate = ? Where BaseCurrencyId = ? AND TargetCurrencyId = ?";
        CurrencyDaoImpl currencyDaoImpl = new CurrencyDaoImpl(connection);

        try (PreparedStatement updateStatement = connection.prepareStatement(sql)){
            updateStatement.setBigDecimal(1, rate);
            updateStatement.setInt(2, currencyDaoImpl.getCurrencyByCode(baseCurrencyCode).getId());
            updateStatement.setInt(3, currencyDaoImpl.getCurrencyByCode(targetCurrencyCode).getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addNewExchangeRate(ExchangeRate exchangeRate) {
        if (isExchangeRateExist(exchangeRate.getBaseCurrency().getId(), exchangeRate.getTargetCurrency().getId())) {
            return;
        }
        String sql = "Insert Into exchange_rate (BaseCurrencyId, TargetCurrencyId, Rate) Values (?,?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
