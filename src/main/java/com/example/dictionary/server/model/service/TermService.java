package com.example.dictionary.server.model.service;

import com.example.dictionary.server.exception.DaoException;
import com.example.dictionary.server.exception.TransactionException;
import com.example.dictionary.server.model.dao.TermDao;
import com.example.dictionary.server.model.entity.Term;
import com.example.dictionary.server.model.pool.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TermService {
    private static final Logger LOGGER = LogManager.getLogger(TermService.class);
    private final TransactionManager transactionManager = TransactionManager.getInstance();

    public List<Term> findAll() {
        List<Term> terms = new ArrayList<>();
        try {
            transactionManager.initializeTransaction();
            Connection connection = transactionManager.getConnection();
            TermDao termDao = new TermDao(connection);
            terms = termDao.findAll();
            transactionManager.commit();
        } catch (TransactionException | DaoException e) {
            LOGGER.error(e.getMessage());
            transactionManager.rollback();
        } finally {
            transactionManager.endTransaction();
        }
        return terms;
    }

    public List<Term> search(String query) {
        List<Term> terms = new ArrayList<>();
        try {
            transactionManager.initializeTransaction();
            Connection connection = transactionManager.getConnection();
            TermDao termDao = new TermDao(connection);
            terms = termDao.search(query);
            transactionManager.commit();
        } catch (TransactionException | DaoException e) {
            LOGGER.error(e.getMessage());
            transactionManager.rollback();
        } finally {
            transactionManager.endTransaction();
        }
        return terms;
    }

    public List<Term> sortByName(boolean ascending) {
        List<Term> terms = new ArrayList<>();
        try {
            transactionManager.initializeTransaction();
            Connection connection = transactionManager.getConnection();
            TermDao termDao = new TermDao(connection);
            if (ascending) {
                terms = termDao.sortByNameAscending();
            } else {
                terms = termDao.sortByNameDescending();
            }
            transactionManager.commit();
        } catch (TransactionException | DaoException e) {
            LOGGER.error(e.getMessage());
            transactionManager.rollback();
        } finally {
            transactionManager.endTransaction();
        }
        return terms;
    }
}
