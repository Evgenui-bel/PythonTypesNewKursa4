package com.example.dictionary.client.controller;

import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.Response;
import com.example.dictionary.server.model.entity.Term;

import java.io.IOException;
import java.util.List;

/**
 * Класс организующий доступ к данным через клиент.
 */
public class ClientController {

    private static final String GET_TERMS_COMMAND = "GET_TERMS";
    private static final String TERMS_KEY = "terms";
    private static final String SEARCH_TERMS = "SEARCH_TERMS";
    private static final String QUERY_KEY = "query";
    private static final String ASCENDING_KEY = "ascending";
    private static final String SORT_BY_NAME = "SORT_TERMS_BY_NAME";

    private final Client client = Client.getInstance();

    /**
     * Возращает все термины.
     */
    public List<Term> getTerms() throws IOException {
        client.startConnection();
        Request clientRequest = new Request(GET_TERMS_COMMAND);
        Response serverResponse = client.sendRequest(clientRequest);
        @SuppressWarnings("unchecked")
        List<Term> terms = (List<Term>) serverResponse.getAttribute(TERMS_KEY);
        client.stopConnection();
        return terms;
    }

    /**
     * Ищет термины.
     */
    public List<Term> search(String query) throws IOException {
        client.startConnection();
        Request clientRequest = new Request(SEARCH_TERMS);
        clientRequest.addParameter(QUERY_KEY, query);
        Response serverResponse = client.sendRequest(clientRequest);
        @SuppressWarnings("unchecked")
        List<Term> terms = (List<Term>) serverResponse.getAttribute(TERMS_KEY);
        client.stopConnection();
        return terms;
    }

    /**
     * Возвращает остортированные термины.
     * ascending == true - по возрастанию, если false - по убыванию.
     */
    public List<Term> sortByName(boolean ascending) throws IOException {
        client.startConnection();
        Request clientRequest = new Request(SORT_BY_NAME);
        clientRequest.addParameter(ASCENDING_KEY, ascending);
        Response serverResponse = client.sendRequest(clientRequest);
        @SuppressWarnings("unchecked")
        List<Term> terms = (List<Term>) serverResponse.getAttribute(TERMS_KEY);
        client.stopConnection();
        return terms;
    }

}
