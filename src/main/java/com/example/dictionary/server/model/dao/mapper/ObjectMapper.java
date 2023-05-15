package com.example.dictionary.server.model.dao.mapper;

import com.example.dictionary.server.model.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс для маппинга результата запроса к БД в Entity-класс.
 */
public interface ObjectMapper<T extends Entity> {
    T mapToObject(ResultSet resultSet) throws SQLException;
}
