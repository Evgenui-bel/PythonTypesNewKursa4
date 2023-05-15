package com.example.dictionary.server.command.impl;

import com.example.dictionary.server.command.Command;
import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.ResponseStatus;
import com.example.dictionary.server.controller.protocol.Response;
import com.example.dictionary.server.model.entity.Term;
import com.example.dictionary.server.model.service.TermService;

import java.util.List;

public class GetTermsCommand implements Command {
    private static final String TERMS_KEY = "terms";

    private final TermService termService;

    public GetTermsCommand(TermService termService) {
        this.termService = termService;
    }

    @Override
    public Response execute(Request clientRequest) {
        List<Term> terms = termService.findAll();
        Response serverResponse = new Response(ResponseStatus.OKAY);
        serverResponse.addAttribute(TERMS_KEY, terms);
        return serverResponse;
    }
}
