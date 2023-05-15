package com.example.dictionary.server.model.dao;

import com.example.dictionary.server.exception.DaoException;
import com.example.dictionary.server.model.dao.mapper.TermMapper;
import com.example.dictionary.server.model.entity.Term;

import java.sql.Connection;
import java.util.List;

/**
 * DAO - Data Access Object. Определяет методы взаимодествия между Entity-классом и базой данных.
 */
public class TermDao {
    /**
     * SQL-запрос на получения всех терминов из базы данных.
     */
    private static final String SELECT_ALL = "SELECT * FROM terms;";
    private static final String SELECT_BY_NAME = "SELECT * FROM terms WHERE name LIKE ?;";
    private static final String SORT_BY_NAME_ASCENDING = "SELECT * FROM terms ORDER BY name;";
    private static final String SORT_BY_NAME_DESCENDING = "SELECT * FROM terms ORDER BY name DESC;";

    private final JdbcTemplate<Term> jdbcTemplate;

    public TermDao(Connection connection) {
        this.jdbcTemplate = new JdbcTemplate<>(new TermMapper(), connection);
    }

    public List<Term> findAll() throws DaoException {
        return jdbcTemplate.executeSelectQuery(SELECT_ALL);
    }

    public List<Term> search(String query) throws DaoException {
        return jdbcTemplate.executeSelectQuery(SELECT_BY_NAME, "%" + query + "%");
    }

    public List<Term> sortByNameAscending() throws DaoException {
        return jdbcTemplate.executeSelectQuery(SORT_BY_NAME_ASCENDING);
    }

    public List<Term> sortByNameDescending() throws DaoException {
        return jdbcTemplate.executeSelectQuery(SORT_BY_NAME_DESCENDING);
    }
}
