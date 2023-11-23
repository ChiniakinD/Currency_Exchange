package daoImp;

import converters.CurrencyValueConverter;
import db.DataBaseConnection;
import dto.CurrencyValueDto;
import entities.CurrencyValue;
import lombok.extern.slf4j.Slf4j;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyDaoImpl  {
    private Connection connection;

    public CurrencyDaoImpl(Connection connection) {
        this.connection = connection;
        log.info("Создано соединение с бд");
    }
    private CurrencyValue mapCurrencyValue(ResultSet resultSet) {
        try{
            CurrencyValue currencyValue = new CurrencyValue();
            currencyValue.setId(resultSet.getInt("id"));
            currencyValue.setCode(resultSet.getString("code"));
            currencyValue.setFullName(resultSet.getString("fullName"));
            currencyValue.setSign(resultSet.getString("sign"));
            return currencyValue;
        } catch (SQLException e) {
            log.error("Ошибка SQL запроса {}", e);
            throw new RuntimeException(e);
        }
    }


    public List<CurrencyValueDto> getAllCurrency() {
        List<CurrencyValueDto> currenciesValueDtoList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM currency";
            log.info("Выбраны данные {}", sql);
            ResultSet resultSet = statement.executeQuery(sql);
            log.info("Создан объект {}", resultSet);
            while (resultSet.next()) {
                currenciesValueDtoList.add(CurrencyValueConverter.entityToDto(mapCurrencyValue(resultSet)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.info("Составлен список {}", currenciesValueDtoList);
        return currenciesValueDtoList;
    }


    public CurrencyValueDto getCurrencyById(int id) {
        String sql = "Select * from Currency where id = ?";
        CurrencyValueDto currencyValueDto = new CurrencyValueDto();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currencyValueDto = CurrencyValueConverter.entityToDto(mapCurrencyValue(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyValueDto;
    }


    public CurrencyValueDto getCurrencyByCode(String code) {
        String sql = "Select * from Currency where code = ?";
        CurrencyValueDto currencyValueDto = new CurrencyValueDto();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currencyValueDto = CurrencyValueConverter.entityToDto(mapCurrencyValue(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyValueDto;
    }

    public void addNewCurrency(CurrencyValueDto currencyValueDto) {
        if (isCurrencyExist(currencyValueDto.getFullName())){
            return;
        }
        CurrencyValue currencyValue = CurrencyValueConverter.dtoToEntity(currencyValueDto);
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
    private boolean isCurrencyExist(String fullName) {
        String sql = "Select * From Currency Where FullName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fullName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
