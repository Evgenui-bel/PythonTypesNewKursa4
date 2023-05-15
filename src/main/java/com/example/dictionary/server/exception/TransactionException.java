package com.example.dictionary.server.exception;

public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException() {
        super();
    }

}
