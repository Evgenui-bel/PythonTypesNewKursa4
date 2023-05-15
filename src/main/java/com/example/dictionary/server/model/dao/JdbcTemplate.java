package com.example.dictionary.server.model.dao;

import com.example.dictionary.server.exception.DaoException;
import com.example.dictionary.server.model.dao.mapper.ObjectMapper;
import com.example.dictionary.server.model.entity.Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс предоставляющий общие операции для Entity-классов с базой данных.
 */
public class JdbcTemplate<T extends Entity> {
    private final ObjectMapper<T> mapper;
    private final Connection connection;

    public JdbcTemplate(ObjectMapper<T> rowMapper, Connection connection) {
        this.mapper = rowMapper;
        this.connection = connection;
    }

    /**
     * Метод для SELECT запросов.
     */
    public List<T> executeSelectQuery(String query, Object... parameters) throws DaoException {
        List<T> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setParametersInPreparedStatement(preparedStatement, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T object = mapper.mapToObject(resultSet);
                list.add(object);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return list;
    }

    /**
     * Метод для SELECT запросов на один обьект.
     */
    public Optional<T> executeSelectQueryForSingleObject(String query, Object... parameters) throws DaoException {
        Optional<T> result = Optional.empty();
        List<T> list = executeSelectQuery(query, parameters);
        if (!list.isEmpty()) {
            result = Optional.of(list.get(0));
        }
        return result;
    }

    /**
     * Перевод из PreparedStatement в Statement. Здесь подставляются параметры в SQL запрос.
     */
    private void setParametersInPreparedStatement(PreparedStatement statement, Object... parameters) throws
            SQLException {
        for (int i = 1; i <= parameters.length; i++) {
            statement.setObject(i, parameters[i - 1]);
        }
    }
}
