package com.example.dictionary.server.controller;

import com.example.dictionary.server.command.Command;
import com.example.dictionary.server.command.CommandFactory;
import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;


/**
 * ОБраотчик запросов клиента, организуемый в отдельном потоке с помощью интерфейса Runnable.
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    /**
     * Сокет клиента.
     */
    private final Socket clientSocket;
    /**
     * Поток для записи обьектов.
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Поток для чтения обьектов.
     */
    private ObjectInputStream objectInputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Жизенный цикл потока ClientHandler.
     */
    @Override
    public void run() {
        try {
            System.out.println("Клиент подключился.");
            initializeObjectInputStream();
            initializeObjectOutputStream();
            processClientRequests();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            endHandling();
            System.out.println("Клиент отключился.");
        }
    }

    /**
     * Обработывает запросы - ClientRequest клиентов, отправляет ответы - ServerResponse.
     */
    private void processClientRequests() throws IOException, ClassNotFoundException {
        try {
            do {
                Request clientRequest = (Request) objectInputStream.readObject();
                String commandName = clientRequest.getCommandName();
                System.out.printf("Клиент отправил запрос: %s%n", commandName);
                Command command = CommandFactory.create(commandName);
                Response response = command.execute(clientRequest);
                objectOutputStream.writeObject(response);
            }
            while (isClientConnected());
        } catch (EOFException ignored) {
        }
    }

    /**
     * Инициализирует потока чтения обьектов.
     */
    private void initializeObjectInputStream() throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    /**
     * Инициализирует потока записи обьектов.
     */
    private void initializeObjectOutputStream() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
    }

    /**
     * Возвращает true если клиент подключен, иначе false.
     */
    private boolean isClientConnected() {
        return clientSocket.isConnected();
    }

    /**
     * Закрывает все потоки и клиентский сокет (разрывает соединение).
     */
    private void endHandling() {
        try {
            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
