package com.example.dictionary.server.model.pool;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Пул соединений с базой данных.
 */
public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * Доступные соединения.
     */
    private final BlockingDeque<ProxyConnection> availableConnections;

    /**
     * Занятые соединения.
     */
    private final BlockingDeque<ProxyConnection> connectionsInUse;
    private static final ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;
    private static final int CONNECTION_POOL_SIZE = 32;

    private ConnectionPool() {
        availableConnections = new LinkedBlockingDeque<>(CONNECTION_POOL_SIZE);
        connectionsInUse = new LinkedBlockingDeque<>(CONNECTION_POOL_SIZE);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        createConnections(connectionFactory);

        if (availableConnections.isEmpty()) {
            LOGGER.fatal("Connections weren't created.");
            throw new RuntimeException("Connections weren't created.");
        } else {
            if (availableConnections.size() < CONNECTION_POOL_SIZE) {
                LOGGER.warn("Connection pool is not full. Trying to fill...");
                createConnections(connectionFactory);
            }
        }
    }

    /**
     * Создаем соединения.
     */
    private void createConnections(ConnectionFactory connectionFactory) {
        Connection connection;
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                connection = connectionFactory.create();
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                boolean isAdded = availableConnections.add(proxyConnection);
                LOGGER.info("Connection add:{}", isAdded);
            } catch (SQLException e) {
                LOGGER.error("Connection wasn't created caused database error.", e);
            }
        }
    }

    public static ConnectionPool getInstance() {
        ConnectionPool localInstance = instance;
        if (localInstance == null) {
            lock.lock();
            localInstance = instance;
            try {
                if (localInstance == null) {
                    localInstance = new ConnectionPool();
                    instance = localInstance;
                }
            } finally {
                lock.unlock();
            }
        }
        return localInstance;
    }

    /**
     * Берёт соединение.
     */
    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = availableConnections.take();
            connectionsInUse.offer(connection);
        } catch (InterruptedException e) {
            LOGGER.error("Current connection thread was interrupted", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    /**
     * Возращает соедиение.
     */
    public void returnConnection(Connection connection) {
        if (connection.getClass() == ProxyConnection.class) {
            try {
                availableConnections.put((ProxyConnection) connection);
                connectionsInUse.remove(connection);
            } catch (InterruptedException e) {
                LOGGER.error("Current connection thread was interrupted", e);
                Thread.currentThread().interrupt();
            }
        } else {
            LOGGER.error("Given connection is not ProxyConnection.");
        }
    }

    /**
     * Освобождение пула соединений.
     */
    public void destroyPool() {
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                availableConnections.take().reallyClose();
            } catch (SQLException | InterruptedException throwables) {
                LOGGER.error("Connection destroying error.");
            }
        }
        deregisterDrivers();
    }

    /**
     * Дерегистрация драйвера.
     */
    private void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOGGER.fatal("Driver wasn't deregistered caused database error.", e);
            }
        }
    }
}
