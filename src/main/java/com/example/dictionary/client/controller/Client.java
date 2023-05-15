package com.example.dictionary.client.controller;

import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.ResponseStatus;
import com.example.dictionary.server.controller.protocol.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Клиент, представляющий собой реализацию шаблона Singleton (только один экзэмпляр класса).
 */
public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);
    private static Client instance;

    /**
     * Сокет подключения к серверу.
     */
    private Socket socket;

    /**
     * Поток для записи обьектов.
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Поток для чтения обьектов.
     */
    private ObjectInputStream objectInputStream;
    /**
     * Конфигурация подключения сервера.
     */
    private ConnectionConfiguration configuration;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    /**
     * Устанавливает конфигурацию подключения к серверу.
     */
    public void configure(ConnectionConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Устанавливает соединение с сервером.
     */
    public void startConnection() throws IOException {
        socket = new Socket(configuration.ip(), configuration.port());
        initializeObjectInputStream();
        initializeObjectOutputStream();
    }

    /**
     * Отправляет запрос на сервер, возвращает ответ от сервера.
     * В случае неудачи, вернет ответ со статусом ERROR.
     */
    public Response sendRequest(Request clientRequest) {
        Response response = new Response(ResponseStatus.ERROR);
        try {
            objectOutputStream.writeObject(clientRequest);
            response = (Response) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error(e.getMessage());
        }
        return response;
    }

    /**
     * Закрывает соединение с сервером.
     */
    public void stopConnection() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
    }

    /**
     * Инициализирует потока чтения обьектов.
     */
    private void initializeObjectInputStream() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    /**
     * Инициализирует потока записи обьектов.
     */
    private void initializeObjectOutputStream() throws IOException {
        InputStream inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
    }
}
