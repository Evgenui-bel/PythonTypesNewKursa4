package com.example.dictionary.server.controller.protocol;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Клиентский запрос.
 */
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 6426829451528307118L;

    /**
     * Имя команды серверу.
     */
    private final String commandName;
    /**
     * Параметры (ключ-значение), отправляемые серверу клиентом.
     */
    private final Map<String, Object> parameters;

    public Request(String command) {
        this.commandName = command;
        this.parameters = new HashMap<>();
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request that = (Request) o;
        return Objects.equals(commandName, that.commandName) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, parameters);
    }

}
