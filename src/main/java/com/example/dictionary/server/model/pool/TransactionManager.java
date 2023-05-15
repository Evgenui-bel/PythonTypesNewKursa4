package com.example.dictionary.server.model.pool;

import com.example.dictionary.server.exception.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Менеджер транзакций. Где транзакция - это процесс обращения к базе данных. Менеджер транзакций реализует шаблон
 * Singleton - только один экзэмпляр класса.
 */
public class TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger(TransactionManager.class);
    /**
     * Экзэмпляр класса.
     */
    private static TransactionManager instance;
    /**
     * Потокобезопасное соединение с базой данных.
     */
    private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * Модификатор private закрывает доступ для создания экзэмпляра класса вне самого класса.
     */
    private TransactionManager() {
    }

    /**
     * Метод предостовляющий ссылку на экзмпляр класса, если таковой уже был создан, иначе его создает.
     */
    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    /**
     * Берет соединение из пула соедиений с базой данных.
     */
    public void initializeTransaction() throws TransactionException {
        if (connectionThreadLocal.get() == null) {
            try {
                Connection connection = ConnectionPool.getInstance().getConnection();
                if (connection == null) {
                    throw new TransactionException("Current thread was interrupted caused by null connection");
                }
                connectionThreadLocal.set(connection);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("Failed to set connection autocommit false.", e);
            }
        }
    }

    /**
     * Возвращает соединение для работы с ним.
     */
    public Connection getConnection() throws TransactionException {
        Connection connection = connectionThreadLocal.get();
        if (connection == null) {
            throw new TransactionException("Can't get connection.");
        }
        return connection;
    }

    /**
     * Конец транзакции - закрывает соединение.
     */
    public void endTransaction() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connectionThreadLocal.remove();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Failed to set connection autocommit true.", e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close connection.", e);
                }
            }
        }
    }

    /**
     * Сохранет все прозиведенные операции в базе данных.
     */
    public void commit() throws TransactionException {
        Connection connection = connectionThreadLocal.get();
        if (connection == null) {
            throw new TransactionException("Can't get connection.");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("Failed to commit connection.", e);
            throw new TransactionException("Failed to commit connection.", e);
        }
    }

    /**
     * Откатывает все произведенные операции в базе данных.
     */
    public void rollback() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error("Failed to rollback connection.", e);
            }
        }

    }

}
