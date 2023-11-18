package daoImp;

import dao.CurrencyDao;
import db.DataBaseConnection;
import entities.CurrencyValue;
import lombok.extern.slf4j.Slf4j;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyDaoImpl implements CurrencyDao {
    private Connection connection;

    public CurrencyDaoImpl(Connection connection) {
        this.connection = connection;
        log.info("создано соединение с бд");
    }

    @Override
    public List<CurrencyValue> getAllCurrency() {
        List<CurrencyValue> currencies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM currency";
            System.out.println("выбраны данные " + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("создан объект " + resultSet);
            while (resultSet.next()) {
                CurrencyValue currencyValue = new CurrencyValue();
                currencyValue.setId(resultSet.getInt("id"));
                currencyValue.setCode(resultSet.getString("code"));
                currencyValue.setFullName(resultSet.getString("fullName"));
                currencyValue.setSign(resultSet.getString("sign"));
                System.out.println("добавлен объект " + currencyValue);
                currencies.add(currencyValue);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("составлен список " + currencies);
        return currencies;
    }

    @Override
    public CurrencyValue getCurrencyById(int id) {
        String sql = "Select * from Currency where id = ?";
        CurrencyValue currencyValue = new CurrencyValue();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currencyValue.setId(resultSet.getInt("id"));
                currencyValue.setCode(resultSet.getString("code"));
                currencyValue.setFullName(resultSet.getString("fullName"));
                currencyValue.setSign(resultSet.getString("sign"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyValue;
    }

    @Override
    public CurrencyValue getCurrencyByCode(String code) {
        String sql = "Select * from Currency where code = ?";
        CurrencyValue currencyValue = new CurrencyValue();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1,code);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    currencyValue.setId(resultSet.getInt("id"));
                    currencyValue.setCode(resultSet.getString("code"));
                    currencyValue.setFullName(resultSet.getString("fullName"));
                    currencyValue.setSign(resultSet.getString("sign"));
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyValue;
    }
        public void addNewCurrency(CurrencyValue currencyValue) {
        String sql = "Insert Into Currency (code, fullName, sign) Values (?,?,?)";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, currencyValue.getCode());
                preparedStatement.setString(2, currencyValue.getFullName());
                preparedStatement.setString(3, currencyValue.getSign());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}
