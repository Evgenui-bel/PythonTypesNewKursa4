package com.example.dictionary.server.command;

import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.Response;

public interface Command {
    Response execute(Request clientRequest);
}
