package com.example.dictionary.server.command.impl;

import com.example.dictionary.server.command.Command;
import com.example.dictionary.server.controller.protocol.Request;
import com.example.dictionary.server.controller.protocol.ResponseStatus;
import com.example.dictionary.server.controller.protocol.Response;
import com.example.dictionary.server.model.entity.Term;
import com.example.dictionary.server.model.service.TermService;

import java.util.List;

public class SortTermsByNameCommand implements Command {
    private static final String TERMS_KEY = "terms";
    private final TermService termService;

    public SortTermsByNameCommand(TermService termService) {
        this.termService = termService;
    }

    @Override
    public Response execute(Request clientRequest) {
        boolean ascending = (boolean) clientRequest.getParameter("ascending");
        List<Term> terms = termService.sortByName(ascending);
        Response serverResponse = new Response(ResponseStatus.OKAY);
        serverResponse.addAttribute(TERMS_KEY, terms);
        return serverResponse;
    }
}
