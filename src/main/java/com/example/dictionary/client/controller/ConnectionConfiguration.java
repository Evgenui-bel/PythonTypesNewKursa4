package com.example.dictionary.client.controller;

import java.util.Objects;

public record ConnectionConfiguration(String ip, int port) {
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionConfiguration that = (ConnectionConfiguration) o;
        return port == that.port && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
