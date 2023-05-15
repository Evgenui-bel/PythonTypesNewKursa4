package com.example.dictionary.server.command;

import com.example.dictionary.server.command.impl.GetTermsCommand;
import com.example.dictionary.server.command.impl.SearchTermsCommand;
import com.example.dictionary.server.command.impl.SortTermsByNameCommand;
import com.example.dictionary.server.model.service.TermService;

public class CommandFactory {
    public static Command create(String commandName) {
        return switch (commandName) {
            case "GET_TERMS" -> new GetTermsCommand(new TermService());
            case "SEARCH_TERMS" -> new SearchTermsCommand(new TermService());
            case "SORT_TERMS_BY_NAME" -> new SortTermsByNameCommand(new TermService());
            default -> throw new IllegalArgumentException("Can't define command.");
        };
    }
}
