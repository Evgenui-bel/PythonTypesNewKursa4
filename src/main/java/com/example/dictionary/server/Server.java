package com.example.dictionary.server;

import com.example.dictionary.server.controller.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Многопользовательский сервер на основе серверного сокета (ServerSocket).
 * Ждет подключения клиентов. Для каждого клиента организует отдельный поток с ClientHandler.
 */
public class Server {

    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Жизненный цикл сервера.
     */
    public void start() {
        System.out.println("Сервер запущен.");
        try {
            while (!serverSocket.isClosed()) {
                System.out.println("Сервер ожидает клиентов...");
                Socket clientSocket = serverSocket.accept();
                startClientThread(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
            System.out.println("Сервер остановлен.");
        }
    }

    /**
     * Создает отдельный поток для обработчика клиентских запросов (ClientHandler).
     */
    private void startClientThread(Socket clientSocket) {
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        Thread clientThread = new Thread(clientHandler);
        clientThread.start();
    }

    /**
     * Закрывает серверный сокет.
     */
    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8080);
        server.start();
    }
}
