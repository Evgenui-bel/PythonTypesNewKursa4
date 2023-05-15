package com.example.dictionary.server.model.dao.mapper;

import com.example.dictionary.server.model.entity.Term;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TermMapper implements ObjectMapper<Term> {
    @Override
    public Term mapToObject(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String information = resultSet.getString("information");
        return new Term(id, name, information);
    }
}
