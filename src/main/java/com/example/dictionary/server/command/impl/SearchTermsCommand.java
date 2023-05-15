package com.example.dictionary.server.command.impl;

import com.example.dictionary.server.command.Command;
import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.ResponseStatus;
import com.example.dictionary.server.controller.protocol.Response;
import com.example.dictionary.server.model.entity.Term;
import com.example.dictionary.server.model.service.TermService;

import java.util.List;

public class SearchTermsCommand implements Command {
    private static final String QUERY_KEY = "query";
    private static final String TERMS_KEY = "terms";

    private final TermService termService;

    public SearchTermsCommand(TermService termService) {
        this.termService = termService;
    }

    @Override
    public Response execute(Request clientRequest) {
        String query = (String) clientRequest.getParameter(QUERY_KEY);
        List<Term> terms = termService.search(query);
        Response serverResponse = new Response(ResponseStatus.OKAY);
        serverResponse.addAttribute(TERMS_KEY, terms);
        return serverResponse;
    }
}
