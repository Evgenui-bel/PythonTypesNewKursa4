package com.example.dictionary.server.controller.protocol;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Ответ от сервера - статус и данные в виде ключ-значения.
 */
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -2305567287646131961L;

    private final ResponseStatus responseStatus;
    private final Map<String, Object> attributes;

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        this.attributes = new HashMap<>();
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response that = (Response) o;
        return responseStatus == that.responseStatus && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus, attributes);
    }
}
